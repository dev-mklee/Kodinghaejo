package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestLngDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestQuestionRepository;
import com.kodinghaejo.entity.repository.TestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	private final BoardRepository boardRepository;
	private final TestQuestionRepository questionRepository;
	private final ReplyRepository replyRepository;
	
	//문제 작성
	@Override
	public void saveTestWrite(TestDTO testDTO) {
		TestEntity testEntity = testDTO.dtoToEntity(testDTO);
		testEntity.setRegdate(LocalDateTime.now());
		testRepository.save(testEntity);
		
		if (!testDTO.getTestLngList().isEmpty()) {
			for (TestLngDTO langDTO : testDTO.getTestLngList()) {
				TestLngEntity langEntity = langDTO.dtoToEntity(langDTO);
				langEntity.setTestIdx(testEntity);
				testLngRepository.save(langEntity);
			}
		}
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
	
	//자유게시판 관리 화면
	@Override
	public List<BoardDTO> freeboardList() {
		List<BoardEntity> boardEntities = boardRepository.findByCatNot("공지사항");
	    List<BoardDTO> boardDTOs = new ArrayList<>();
	    
	    for (BoardEntity board : boardEntities) {
	        BoardDTO boardDTO = new BoardDTO(board);
	        boardDTOs.add(boardDTO);
	    }
	    
	    return boardDTOs;
		
	}
	
	//공지사항 관리 화면
	@Override
	public List<BoardDTO> noticeboardList() {
		List<BoardEntity> boardEntities = boardRepository.findByCat("공지사항");
		List<BoardDTO> boardDTOs = new ArrayList<>();
		
		for (BoardEntity board : boardEntities) {
	        BoardDTO boardDTO = new BoardDTO(board);
	        boardDTOs.add(boardDTO);
	    }
	    
	    return boardDTOs;
		
	}
	
	//공지사항 작성
	@Override
	public void write(BoardDTO board) {
		board.setRegdate(LocalDateTime.now());
		board.setHitCnt(0);
		boardRepository.save(board.dtoToEntity(board));	
	}
	//게시글 삭제(자유게시판,공지사항)
	@Override
	public void deleteBoard(Long idx) {
		BoardEntity boardEntity = boardRepository.findById(idx).get();
		boardRepository.delete(boardEntity);
	}
	
	//질문게시판 관리 화면
	@Override
	public List<TestQuestionDTO> questionList() {
		List<TestQuestionEntity> questionEntities = questionRepository.findAll();
		List<TestQuestionDTO> questionDTOs = new ArrayList<>();
		
		for (TestQuestionEntity question : questionEntities) {
			TestQuestionDTO questionDTO = new TestQuestionDTO(question);
			questionDTOs.add(questionDTO);
		}
		return questionDTOs;
	}
	//질문 삭제
	@Override
	public void deleteQBoard(Long idx) {
		TestQuestionEntity questionEntity = questionRepository.findById(idx).get();
		questionRepository.delete(questionEntity);
	}
	
	
	//댓글 관리 화면
	@Override
	public List<ReplyDTO> replyList() {
		List<ReplyEntity> replyEntities = replyRepository.findAll();
		List<ReplyDTO> replyDTOs = new ArrayList<>();
		
		for (ReplyEntity reply : replyEntities) {
			ReplyDTO replyDTO = new ReplyDTO(reply);
			replyDTOs.add(replyDTO);
		}
		return replyDTOs;
	}
	
	//댓글 삭제
	@Override
	public void deleteReply(Long idx) {
		ReplyEntity replyEntity = replyRepository.findById(idx).get();
		replyRepository.delete(replyEntity);
	}
}