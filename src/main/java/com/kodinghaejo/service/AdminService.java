package com.kodinghaejo.service;

import java.util.List;
import java.util.Map;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.CommonCodeDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

	//문제 작성
	void saveTestWrite(TestDTO testDTO);
	
	//문제 보여주기
	public List<TestDTO> testAllList();
	

	//문제 수정
	public void saveTestModify(TestDTO testDTO);
	
	//ID로 문제 데이터 조회
	public TestDTO getTestById(Long id);
	
	//회원정보 관리화면
	public List<MemberDTO> memberAllList();
	
	//자유게시판 관리화면
	public List<BoardDTO> freeboardList();
	
	//공지사항 관리화면
	public List<BoardDTO> noticeboardList();
	
	//공지사항 작성
	public void write(BoardDTO board);
	
	//ID로 공지사항 데이터 조회
	public BoardDTO getNoticeById(Long id);
	
	//공지사항 수정
	public void savenoticeModify(BoardDTO boardDTO);
	
	//질문게시판 관리화면
	public List<TestQuestionDTO> questionList();
	
	//댓글 관리화면
	public List<ReplyDTO> replyList();
	
	//채팅방 관리화면
	public List<ChatDTO> chatList();
	
	//참여인원 0인 채팅방 삭제
	public void deleteEmptyChats();
	
	//게시글 삭제(자유게시판,공지사항)
	public void deleteBoard(Long idx);
	
	//게시글 삭제(질문게시판)
	public void deleteQBoard(Long idx);
	
	//댓글 삭제
	public void deleteReply(Long idx);
	
	//문제 검색
	public List<TestDTO> searchtestListByTitle(String searchKeyword);
	
	//회원정보 검색
	public List<MemberDTO> searchMembers(String searchType, String searchKeyword);
	
	//자유게시판 검색
	public List<BoardDTO> searchFreeboardListByTitle(String searchKeyword);
	
	//공지사항 검색
	public List<BoardDTO> searchNoticeListByTitle(String searchKeyword);
	
	//질문게시판 검색
	public List<TestQuestionDTO> searchQboardListByTitle(String searchKeyword);
	
	//댓글 검색
	public List<ReplyDTO> searchReplyListByContent(String searchKeyword);
	
	//채팅방 검색
	public List<ChatDTO> searchChatListByTitle(String searchKeyword);

	
	//일별가입자수 체크
	public long getTodaySignups();
	
	//일별 자유게시판 작성 수
	public long getTodayFreeBoardCount();
	
	//일별 방문자 수 체크
	public long getTodayVisitorCount(HttpServletRequest request);
	
	//일별 방문자 수 증가
	public void upTodayVisitorCount(HttpServletRequest request);
	
	//방문자 IP
	public String getUserIp(HttpServletRequest request);
	
	//일별 푼 문제 수
	public long getTodayTestCount();
	
	//월별 가입자수 체크
	public Map<Integer, Long> getMonthlySignups();
	
	//문제풀이에 사용된 언어
	public Map<String, Integer> getLngSubmitCount();
	
	//회원탈퇴
	public void deleteMember(String email);
	
	//공통코드 관리화면
	public List<CommonCodeDTO> codeList();
	
	//공통코드 검색
	public List<CommonCodeDTO> searchCodeListByCode(String searchKeyword);
	
	//공통코드 필터 타입
	public List<CommonCodeDTO> getCodeListByType(String type);
	
	//공통코드 추가
	public void codewrite(CommonCodeDTO code);
	
	//공통코드 삭제
	public boolean deleteCommonCode(String code);
}


