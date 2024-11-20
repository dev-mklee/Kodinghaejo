package com.kodinghaejo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.entity.FileEntity;

public interface MemberService {

	//회원가입
	public void join(MemberDTO member);

	//기본정보 수정
	public void editMemberInfo(MemberDTO member);

	//주요 기술 변경(마이 페이지)
	public void editTec(String email, String tec1, String tec2, String tec3);

	//희망 직무 변경(마이 페이지)
	public void editJob(String email, String job1, String job2, String job3);

	//비밀번호 변경
	//비밀번호 찾기(임시비밀번호 발급)
	public void editPassword(MemberDTO member);

	//이메일 중복 확인
	public int checkEmail(String email);

	//회원 기본정보
	public MemberDTO memberInfo(String email);

	//회원 로그인, 로그아웃, 패스워드변경 일자 등록(Update)
	public void lastdateUpdate(String email, String status);

	//회원 로그 등록
	public void memberLogRegistry(String email, String status);

	//회원 아이디(이메일) 찾기
	public String findId(MemberDTO member);

	//비밀번호 변경일 연기(30일)
	public void editPasswordAfter30(String email);

	//회원 첨부파일 목록
	public List<FileEntity> getMemberFileList(String email);

	//본인이 방장인 채팅방의 갯수 확인
	public Long countChatManager(String email);

	//계정 삭제
	public void deleteAccount(String email);

	//내가 작성한 게시글
	public Page<BoardDTO> mypageBoardList(String email, int pageNum, int postNum);

	//내가 작성한 댓글
	Page<ReplyDTO> mypageReplyList(String email, int pageNum, int postNum);

}