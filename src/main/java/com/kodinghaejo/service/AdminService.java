package com.kodinghaejo.service;

import java.util.List;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;

public interface AdminService {

	//문제 작성
	public void saveTestWrite(TestDTO test);
	
	//문제 보여주기
	public List<TestDTO> testAllList();
	
	//자유게시판 관리화면
	public List<BoardDTO> freeboardList();
	
	//공지사항 관리화면
	public List<BoardDTO> noticeboardList();
	
	//공지사항 작성
	public void write(BoardDTO board);
	
	//질문게시판 관리화면
	public List<TestQuestionDTO> questionList();
	
	//댓글 관리화면
	public List<ReplyDTO> replyList();
	
	//게시글 삭제(자유게시판,공지사항)
	public void deleteBoard(Long idx);
	
	//게시글 삭제(질문게시판)
	public void deleteQBoard(Long idx);
	
	//댓글 삭제
	public void deleteReply(Long idx);
}