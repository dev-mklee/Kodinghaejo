package com.kodinghaejo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestLngDTO;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestBookmarkEntityID;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestSubmitEntity;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.TestBookmarkRepository;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.entity.repository.TestSubmitRepository;
import com.kodinghaejo.util.ReplaceUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {

	private final MemberRepository memberRepository;
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	private final TestSubmitRepository testSubmitRepository;
	private final TestBookmarkRepository bookmarkRepository;

	//코딩테스트 문제 목록
	@Override
	public Page<TestDTO> getTestList(int pageNum, int postNum, String email, String keyword, String submSts, String lng,
			String diff) {
		MemberEntity memberEntity = (email == null) ? null : memberRepository.findById(email).get();

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		List<TestEntity> testEntities = testRepository.findByTitleContainingAndIsUse(keyword.trim(), "Y");

		//상태 필터가 적용되어 있을 경우 필터링
		if (email != null && submSts != null && !submSts.equals("")) {
			String[] submStsArr = submSts.split(",");
			List<TestEntity> submStsList = new ArrayList<>();

			for (String sS : submStsArr) {
				switch (sS.trim()) {
				case "U": //안 푼 문제
					List<TestLngEntity> temps = testLngRepository.findAll();
					for (TestLngEntity temp : temps)
						if (testSubmitRepository.countByTlIdxAndEmail(temp, memberEntity) == 0)
							submStsList.add(temp.getTestIdx());

					break;

				case "N":
				case "Y": //풀고 있는 문제, 푼 문제
					testSubmitRepository.findByEmailAndSubmSts(memberEntity, submSts).stream()
							.forEach((e) -> submStsList.add(e.getTlIdx().getTestIdx()));
					break;
				}
			}

			testEntities.retainAll(submStsList);
		}

		//언어 필터가 적용되어 있을 경우 필터링
		if (lng != null && !lng.equals("")) {
			String[] lngArr = lng.split(",");
			List<TestEntity> lngList = new ArrayList<>();

			for (String l : lngArr)
				testLngRepository.findByLng(l.replaceAll(" ", "").trim()).stream().forEach((e) -> lngList.add(e.getTestIdx()));

			testEntities.retainAll(lngList);
		}

		//난이도 필터가 적용되어 있을 경우 필터링
		if (diff != null && !diff.equals("")) {
			String[] diffArr = diff.split(",");
			List<TestEntity> diffList = new ArrayList<>();

			for (String d : diffArr)
				diffList.addAll(testRepository.findByDiff(Integer.parseInt(d.replaceAll(" ", "").trim())));

			testEntities.retainAll(diffList);
		}

		List<TestDTO> testDTOs = new ArrayList<>();

		for (TestEntity testEntity : testEntities) {
			TestDTO test = new TestDTO(testEntity);
			List<TestLngEntity> testLngEntities = testLngRepository.findByTestIdx(testEntity);

			//문제 상태 확인
			if (email == null) {
				test.setSubmSts("U");
			} else {
				long submY = 0L; //푼 문제
				long submN = 0L; //풀고 있는 문제
				for (TestLngEntity testLngEntity : testLngEntities) {
					submY += testSubmitRepository.countByTlIdxAndEmailAndSubmSts(testLngEntity, memberEntity, "Y");
					submN += testSubmitRepository.countByTlIdxAndEmailAndSubmSts(testLngEntity, memberEntity, "N");
				}

				test.setSubmSts((submY > 0L) ? "Y" : (submN > 0L) ? "N" : "U");
			}

			//완료한 사람, 정답률 확인
			long correctCount = 0; //완료한 사람 수
			long submitCount = 0; //제출한 사람 전체 수
			for (TestLngEntity testLngEntity : testLngEntities) {
				correctCount += testSubmitRepository.countByTlIdxAndSubmSts(testLngEntity, "Y");
				submitCount += testSubmitRepository.countByTlIdx(testLngEntity);
			}
			test.setCorrectCount(correctCount);
			test.setSubmitCount(submitCount);
			test.setCorrectRate((correctCount == 0) ? 0 : ((double) correctCount / (double) submitCount * 100));

			testDTOs.add(test);
		}

		int startPoint = (int) pageRequest.getOffset();
		int endPoint = (startPoint + pageRequest.getPageSize()) > testDTOs.size() ? testDTOs.size()
				: (startPoint + pageRequest.getPageSize());

		return new PageImpl<>(testDTOs.subList(startPoint, endPoint), pageRequest, testDTOs.size());
	}

	//코딩테스트 문제 가져오기
	@Override
	public TestDTO loadTest(Long idx) throws Exception {
		return testRepository.findById(idx).map((test) -> new TestDTO(test)).get();
	}

	//문제에서 선택 가능한 언어 확인
	@Override
	public String lngAvlChk(Long idx, String lng) throws Exception {
		TestEntity testEntity = testRepository.findById(idx).get();

		return testLngRepository.findFirstByTestIdxAndLng(testEntity, lng).isPresent() ? "Y" : "N";
	}

	//코딩테스트 언어별 문제 가져오기
	@Override
	public TestLngEntity loadTestLng(Long testIdx, String language) throws Exception {
		TestEntity testEntity = testRepository.findById(testIdx).get();

		return testLngRepository.findFirstByTestIdxAndLng(testEntity, language).get();
	}

	//코드 제출 시 파일 생성
	@Override
	public void createVerifyFiles(String mainSrc, String correctSrc, String language) throws Exception {
		String mainPath = "submissions/Main." + language;
		String correctPath = "submissions/Verify." + language;

		BufferedWriter mainWriter = new BufferedWriter(new FileWriter(mainPath));
		BufferedWriter correctWriter = new BufferedWriter(new FileWriter(correctPath));

		mainWriter.write(new ReplaceUtil().unescapeHtml(mainSrc));
		correctWriter.write(new ReplaceUtil().unescapeHtml(correctSrc));

		mainWriter.close();
		correctWriter.close();
	}

	//코드 제출 시 검증 처리
	@Override
	public String testCode(String language, String filePath) throws Exception {
		String absolutePath = new File(filePath).getAbsolutePath(); //절대 경로로 변환
		String imageName;
		String containerName;

		if (language.equals("java") || language.equals("js")) {
			imageName = language + "-code";
			containerName = language + "-container";
		} else {
			throw new IllegalArgumentException("지원하지 않는 언어: " + language);
		}

		//빌드 명령어
		String buildCommand = "docker build -t " + imageName + " -f submissions/Dockerfile." + language + " submissions/";

		//컨테이너 실행 명령어
		String runCommand = "docker run --name " + containerName + " --rm --memory=512m --cpus=0.5 -v " + absolutePath
				+ ":/app/Solution." + language + " " + imageName;

		//Docker 빌드 명령어 실행
		ProcessBuilder buildProcessBuilder = new ProcessBuilder(buildCommand.split(" "));
		Process buildProcess = buildProcessBuilder.start();
		buildProcess.waitFor(); //빌드 완료 대기

		if (buildProcess.exitValue() != 0) {
			return getProcessErrorOutput(buildProcess);
		}

		//Docker 컨테이너 실행 명령어 실행
		ProcessBuilder runProcessBuilder = new ProcessBuilder(runCommand.split(" "));
		Process runProcess = runProcessBuilder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
		StringBuilder output = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line).append("\n");
		}

		runProcess.waitFor(); //프로세스 종료 대기

		if (runProcess.exitValue() == 0) {
			return output.toString();
		} else {
			return getProcessErrorOutput(runProcess);
		}
	}

	//코드 검증 처리 중 에러 발생 시 처리 메소드
	private String getProcessErrorOutput(Process process) throws Exception {
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		StringBuilder errorOutput = new StringBuilder();
		String line;
		while ((line = errorReader.readLine()) != null) {
			errorOutput.append(line).append("\n");
		}
		return "Error:\n" + errorOutput.toString();
	}

	//코드 제출 처리
	@Override
	public boolean submitTest(Long tlIdx, String email, String submSts, String code) {
		TestLngEntity testLngEntity = testLngRepository.findById(tlIdx).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();

		TestSubmitEntity testSubmitEntity;
		boolean isAdd = (testSubmitRepository.countByTlIdxAndEmailAndSubmSts(testLngEntity, memberEntity, submSts) == 0);

		if (isAdd) {
			testSubmitEntity = TestSubmitEntity
													.builder()
													.tlIdx(testLngEntity)
													.email(memberEntity)
													.submSts(submSts)
													.content(code)
													.regdate(LocalDateTime.now())
													.build();
		} else {
			testSubmitEntity = testSubmitRepository.findByTlIdxAndEmailAndSubmSts(testLngEntity, memberEntity, submSts).get();
			testSubmitEntity.setContent(code);
			testSubmitEntity.setRegdate(LocalDateTime.now());
		}

		testSubmitRepository.save(testSubmitEntity);

		return isAdd;
	}

	//언어별 문제 인덱스로 불러오기
	@Override
	public TestLngEntity loadTestLngByIdx(Long idx) throws Exception {
		return testLngRepository.findById(idx).get();
	}

	//가장 많이 풀어본 문제 idx값 가져오기
	@Override
	public Long getMostPopularTest() {
		//가장 인기 있는 tl_idx 값을 찾아서
		Long mostPopularTlIdx = testSubmitRepository.findMostPopularTlIdx();

		return testLngRepository.findTestIdxByTlIdx(mostPopularTlIdx);
	}

	//등록일 기준 신규 문제 idx값 가져오기
	@Override
	public Long getNewTest(int count) {
//		Pageable pageable = PageRequest.of(0, count);
//
//		List<TestEntity> newProblem = testRepository.findNewTest(pageable);
		List<TestEntity> newProblem = testRepository.findByIsUseOrderByRegdateDesc("Y");

		Random random = new Random();
		int randomIdx = random.nextInt(newProblem.size());

		return newProblem.get(randomIdx).getIdx();
	}

	//난이도가 0인 문제 idx값 가져오기
	@Override
	public Long getRandomTest() {
		List<TestEntity> diffZero = testRepository.findByDiff(0);

		Random random = new Random();
		int randomTest = random.nextInt(diffZero.size());

		return diffZero.get(randomTest).getIdx();
	}

	//난이도별 문제 보기
	@Override
	public List<TestDTO> getDiffTest() {
		List<TestEntity> diffTest = new ArrayList<>();

		List<TestEntity> diffZero = testRepository.findByDiff(0);
		List<TestEntity> diffOne = testRepository.findByDiff(1);
		List<TestEntity> diffTwo = testRepository.findByDiff(2);

		if (!diffZero.isEmpty()) {
			diffTest.add(getRandomList(diffZero));
		}
		if (!diffOne.isEmpty()) {
			diffTest.add(getRandomList(diffOne));
		}
		if (!diffTwo.isEmpty()) {
			diffTest.add(getRandomList(diffTwo));
		}

		List<TestDTO> testDTOs = new ArrayList<>();

		for (TestEntity test : diffTest) {
			TestDTO testDTO = new TestDTO(test);

			List<TestLngDTO> testLngDTOs = new ArrayList<>();
			testLngRepository.findByTestIdx(test).stream().forEach((e) -> testLngDTOs.add(new TestLngDTO(e)));

			testDTO.setTestLngList(testLngDTOs);

			testDTOs.add(testDTO);
		}

		return testDTOs;
	}

	//문제 리스트 중 랜덤으로 하나 선택
	@Override
	public TestEntity getRandomList(List<TestEntity> problem) {
		Random random = new Random();
		int randomTest = random.nextInt(problem.size());

		return problem.get(randomTest);
	}

	//문제 보여주기
	@Override
	public List<TestDTO> testAllList() {
		List<TestEntity> testEntities = testRepository.findAll(Sort.by("idx").descending()); //문제 목록 조회
		List<TestDTO> testDTOList = new ArrayList<>();

		for (TestEntity test : testEntities) {
			TestDTO testDTO = new TestDTO(test); //기존의 TestEntity 정보를 TestDTO로 변환

			//해당 문제에 대한 언어 정보 조회
			List<TestLngEntity> testLangs = testLngRepository.findByTestIdx(test); //testIdx로 언어 정보 조회
			List<TestLngDTO> testLngDTOs = new ArrayList<>();

			for (TestLngEntity lang : testLangs) {
				TestLngDTO langDTO = new TestLngDTO();
				langDTO.setLng(lang.getLng()); //언어 코드만 추가
				testLngDTOs.add(langDTO); //언어 DTO 목록에 추가
			}

			testDTO.setTestLngList(testLngDTOs); //TestDTO에 언어 정보 세팅
			testDTOList.add(testDTO); //최종 리스트에 추가
		}

		return testDTOList;
	}

	//문제 검색
	@Override
	public List<TestDTO> searchtestListByTitle(String searchKeyword) {
		List<TestEntity> testEntities = testRepository.findByTitleContaining(searchKeyword);
		List<TestDTO> testDTOs = new ArrayList<>();

		for (TestEntity test : testEntities) {
			TestDTO testDTO = new TestDTO(test);

			List<TestLngEntity> testLangs = testLngRepository.findByTestIdx(test); //testIdx로 언어 정보 조회
			List<TestLngDTO> testLngDTOs = new ArrayList<>();

			for (TestLngEntity lang : testLangs) {
				TestLngDTO langDTO = new TestLngDTO();
				langDTO.setLng(lang.getLng());
				testLngDTOs.add(langDTO);
			}

			testDTO.setTestLngList(testLngDTOs);
			testDTOs.add(testDTO);
		}

		return testDTOs;
	}

	//북마크 상태 확인
	@Override
	public String isBookmarked(String email, Long testIdx) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		TestEntity testEntity = testRepository.findById(testIdx).get();

		Long count = bookmarkRepository.countByEmailAndTestIdxAndIsUse(memberEntity, testEntity, "Y");
		return (count == 0) ? "N" : "Y";
	}

	//북마크 추가 및 제거
	@Override
	public void bookmark(String email, Long idx, String kind) {
		TestBookmarkEntity testBookmarkEntity;
		
		if (bookmarkRepository.findById(new TestBookmarkEntityID(email, idx)).isEmpty()) {
			testBookmarkEntity = TestBookmarkEntity
														.builder()
														.email(memberRepository.findById(email).get())
														.testIdx(testRepository.findById(idx).get())
														.addChk(kind)
														.addDate(LocalDateTime.now())
														.isUse("Y")
														.build();
		} else {
			testBookmarkEntity = bookmarkRepository.findById(new TestBookmarkEntityID(email, idx)).get();
			testBookmarkEntity.setAddChk(kind);
			testBookmarkEntity.setAddDate(LocalDateTime.now());
		}

		bookmarkRepository.save(testBookmarkEntity);
	}

	//문제의 난이도 출력
	@Override
	public int getTestDiff(Long tlIdx) {
//		return testSubmitRepository.findDiffByTlIdx(tlIdx);
		return testLngRepository.findById(tlIdx).get().getTestIdx().getDiff();
	}

	//회원의 점수 업데이트
	@Override
	public void updateMemberScore(String email, long scoreToAdd) {
		MemberEntity memberEntity = memberRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("email not found"));
		if (memberEntity != null) {
			memberEntity.setScore(memberEntity.getScore() + scoreToAdd);
			memberRepository.save(memberEntity);
		}
	}

}