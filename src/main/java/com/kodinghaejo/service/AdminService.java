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
	
	//질문게시판 관리화면
	public List<TestQuestionDTO> questionList();
	
	//댓글 관리화면
	public List<ReplyDTO> replyList();
}