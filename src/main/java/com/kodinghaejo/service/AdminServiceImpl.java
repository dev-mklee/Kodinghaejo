package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.ChatMemberDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestLngDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.ChatMemberRepository;
import com.kodinghaejo.entity.repository.ChatRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
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
	private final ChatRepository chatRepository;
	private final ChatMemberRepository chatMemberRepository;
	private final MemberRepository memberRepository;
	
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
	
	//회원정보 관리 화면
	@Override
	public List<MemberDTO> memberAllList() {
		List<MemberEntity> memberEntities = memberRepository.findAll();
		List<MemberDTO> memberDTOs = new ArrayList<>();
		
		for (MemberEntity member : memberEntities) {
			MemberDTO memberDTO = new MemberDTO(member);
			memberDTOs.add(memberDTO);
		}
		return memberDTOs;
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
	
	//채팅방 관리 화면
	@Override
	public List<ChatDTO> chatList() {
		List<ChatEntity> chatEntities = chatRepository.findAll();
		List<ChatDTO> chatDTOs = new ArrayList<>();
		
		for (ChatEntity chat : chatEntities) {
			ChatDTO chatDTO = new ChatDTO(chat);
			chatDTOs.add(chatDTO);
		}
		return chatDTOs;
	}
	
	//댓글 삭제
	@Override
	public void deleteReply(Long idx) {
		ReplyEntity replyEntity = replyRepository.findById(idx).get();
		replyRepository.delete(replyEntity);
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
	
	//회원정보 검색
	public List<MemberDTO> searchMembers(String searchType, String searchKeyword) {
	    List<MemberEntity> memberEntities;

	    // 검색어가 없을 경우 전체 회원 목록 조회
	    if (searchKeyword == null || searchKeyword.isEmpty()) {
	        memberEntities = memberRepository.findAll();
	    } else {
	        // 검색어가 있을 경우 조건에 따라 검색
	        switch (searchType) {
	            case "email":
	                memberEntities = memberRepository.findByEmailContaining(searchKeyword);
	                break;
	            case "nickname":
	                memberEntities = memberRepository.findByNicknameContaining(searchKeyword);
	                break;
	            case "name":
	                memberEntities = memberRepository.findByUsernameContaining(searchKeyword);
	                break;
	            default:
	                memberEntities = new ArrayList<>(); // 검색 조건이 잘못된 경우 빈 리스트 반환
	        }
	    }
	    List<MemberDTO> memberDTOs = new ArrayList<>();
	    for (MemberEntity member : memberEntities) {
	    	MemberDTO memberDTO = new MemberDTO(member);
	        memberDTOs.add(memberDTO);
	    }
	    
	    return memberDTOs;
	}
	
	
	//자유게시판 검색
	public List<BoardDTO> searchFreeboardListByTitle(String searchKeyword) {
		List<BoardEntity> boardEntities = boardRepository.findByTitleContainingAndCatNot(searchKeyword, "공지사항");
		List<BoardDTO> boardDTOs = new ArrayList<>();
		
		for (BoardEntity board : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(board);
			boardDTOs.add(boardDTO);
		}
		
		return boardDTOs;
	}
	
	//공지 검색
	public List<BoardDTO> searchNoticeListByTitle(String searchKeyword) {
		List<BoardEntity> boardEntities = boardRepository.findByTitleContainingAndCat(searchKeyword, "공지사항");
		List<BoardDTO> boardDTOs = new ArrayList<>();
		
		for (BoardEntity board : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(board);
			boardDTOs.add(boardDTO);
		}
			
		return boardDTOs;
	}
	
	//질문게시판 검색
	public List<TestQuestionDTO> searchQboardListByTitle(String searchKeyword) {
		List<TestQuestionEntity> questionEntities = questionRepository.findByTitleContaining(searchKeyword);
		List<TestQuestionDTO> questionDTOs = new ArrayList<>();
		
		for (TestQuestionEntity question : questionEntities) {
			TestQuestionDTO questionDTO = new TestQuestionDTO(question);
			questionDTOs.add(questionDTO);
		}
		
		return questionDTOs;
	}
	
	//댓글 검색
	public List<ReplyDTO> searchReplyListByContent(String searchKeyword) {
		List<ReplyEntity> replyEntities = replyRepository.findByContentContaining(searchKeyword);
		List<ReplyDTO> replyDTOs = new ArrayList<>();
		
		for (ReplyEntity reply : replyEntities) {
			ReplyDTO replyDTO = new ReplyDTO(reply);
			replyDTOs.add(replyDTO);
		}
		
		return replyDTOs;
	}
	
	//채팅방 검색
	public List<ChatDTO> searchChatListByTitle(String searchKeyword) {
		List<ChatEntity> chatEntities = chatRepository.findByTitleContaining(searchKeyword);
		List<ChatDTO> chatDTOs = new ArrayList<>();
		
		for (ChatEntity chat : chatEntities) {
			ChatDTO chatDTO = new ChatDTO(chat);
			chatDTOs.add(chatDTO);
		}
		
		return chatDTOs;
	}
	
	//채팅방 멤버
	public List<ChatMemberDTO> getChatMembers() {
        // 채팅 멤버 정보를 DB에서 가져옵니다.
        List<ChatMemberEntity> chatMemberEntities = chatMemberRepository.findAll();

        // Entity를 DTO로 변환
        List<ChatMemberDTO> chatMemberDTOs = new ArrayList<>();
        for (ChatMemberEntity entity : chatMemberEntities) {
            ChatMemberDTO dto = new ChatMemberDTO(entity);
            chatMemberDTOs.add(dto);
        }

        return chatMemberDTOs;
    }
}