package com.kodinghaejo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.TestDTO;

import com.kodinghaejo.dto.TestLngDTO;

import com.kodinghaejo.entity.MemberEntity;

import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestSubmitEntity;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.entity.repository.TestSubmitRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {
	
	private final MemberRepository memberRepository;
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	private final TestSubmitRepository testSubmitRepository;
	
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
	public void createVerifyFiles(String mainSrc, String correctSrc) throws Exception {
		String mainPath = "submissions/Main.java";
		String correctPath = "submissions/Verify.java";
		
		BufferedWriter mainWriter = new BufferedWriter(new FileWriter(mainPath));
		BufferedWriter correctWriter = new BufferedWriter(new FileWriter(correctPath));
		
		mainWriter.write(mainSrc);
		correctWriter.write(correctSrc);
		
		mainWriter.close();
		correctWriter.close();
	}
	
	//코드 제출 시 검증 처리 
	@Override
	public String testCode(String language, String filePath) throws Exception {
		String absolutePath = new File(filePath).getAbsolutePath(); // 절대 경로로 변환
		String imageName;
		String containerName;

		if (language.equals("java") || language.equals("js")) {
			imageName = language + "-code";
			containerName = language + "-container";
		} else {
			throw new IllegalArgumentException("지원하지 않는 언어: " + language);
		}

		// 빌드 명령어
		String buildCommand = "docker build -t " + imageName + " -f submissions/Dockerfile." + language + " submissions/";

		// 컨테이너 실행 명령어
		String runCommand = "docker run --name " + containerName + " --rm --memory=512m --cpus=0.5 -v "
							+ absolutePath + ":/app/Solution." + language + " " + imageName;

		// Docker 빌드 명령어 실행
		ProcessBuilder buildProcessBuilder = new ProcessBuilder(buildCommand.split(" "));
		Process buildProcess = buildProcessBuilder.start();
		buildProcess.waitFor(); // 빌드 완료 대기

		if (buildProcess.exitValue() != 0) {
			return getProcessErrorOutput(buildProcess);
		}

		// Docker 컨테이너 실행 명령어 실행
		ProcessBuilder runProcessBuilder = new ProcessBuilder(runCommand.split(" "));
		Process runProcess = runProcessBuilder.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
		StringBuilder output = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line).append("\n");
		}

		runProcess.waitFor(); // 프로세스 종료 대기

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
	
	//문제 보여주기
		@Override
		public List<TestDTO> testAllList() {
			List<TestEntity> testEntities = testRepository.findAll(); // 문제 목록 조회
			List<TestDTO> testDTOList = new ArrayList<>();
			
			for (TestEntity test : testEntities) {
				TestDTO testDTO = new TestDTO(test);  // 기존의 TestEntity 정보를 TestDTO로 변환
				
				// 해당 문제에 대한 언어 정보 조회
				List<TestLngEntity> testLangs = testLngRepository.findByTestIdx(test); // testIdx로 언어 정보 조회
				List<TestLngDTO> testLngDTOs = new ArrayList<>();
				
				for (TestLngEntity lang : testLangs) {
					TestLngDTO langDTO = new TestLngDTO();
					langDTO.setLng(lang.getLng());  // 언어 코드만 추가
					testLngDTOs.add(langDTO);  // 언어 DTO 목록에 추가
				}
				
				testDTO.setTestLngList(testLngDTOs);  // TestDTO에 언어 정보 세팅
				testDTOList.add(testDTO);  // 최종 리스트에 추가
			}
			
			return testDTOList;
		}
			
		//문제 검색
		public List<TestDTO> searchtestListByTitle(String searchKeyword) {
			List<TestEntity> testEntities = testRepository.findByTitleContaining(searchKeyword);
			List<TestDTO> testDTOs = new ArrayList<>();
			
			for (TestEntity test : testEntities) {
				TestDTO testDTO = new TestDTO(test);
				
				List<TestLngEntity> testLangs = testLngRepository.findByTestIdx(test); // testIdx로 언어 정보 조회
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

	//코드 제출 처리
	@Override
	public void submitTest(Long tlIdx, String email, String submSts, String code) {
		TestLngEntity testLngEntity = testLngRepository.findById(tlIdx).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		TestSubmitEntity testSubmitEntity = TestSubmitEntity
																					.builder()
																					.tlIdx(testLngEntity)
																					.email(memberEntity)
																					.submSts(submSts)
																					.content(code)
																					.regdate(LocalDateTime.now())
																					.build();
		
		testSubmitRepository.save(testSubmitEntity);
	}
	
}