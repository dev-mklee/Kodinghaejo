package com.kodinghaejo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.CommonCodeDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.CommonCodeEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.service.AdminService;
import com.kodinghaejo.util.PageUtil;
import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class AdminController {
	
	private final AdminService service;
	
	
	//시스템 관리 메인화면
	@GetMapping("/admin/systemMain")
	public String getSystemMain(Model model, HttpServletRequest request) {
		long todaySignups = service.getTodaySignups();
		model.addAttribute("todaySignups", todaySignups);
		
		long todayVisitorCount = service.getTodayVisitorCount(request);
		model.addAttribute("todayVisitorCount", todayVisitorCount);
		
		long todayFreeboardCount = service.getTodayFreeBoardCount();
		model.addAttribute("todayFreeboardCount", todayFreeboardCount);
		
		long todayTestCount = service.getTodayTestCount();
		model.addAttribute("todayTestCount", todayTestCount);
		
		Map<Integer, Long> monthlySignups = service.getMonthlySignups();
		String monthlySignupsJson = new Gson().toJson(monthlySignups);
		model.addAttribute("monthlySignupsJson", monthlySignupsJson);
		
		Map<String, Integer> lngCount = service.getLngSubmitCount();
		try {
			model.addAttribute("lngCount", new ObjectMapper().writeValueAsString(lngCount));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("lngCount", "{}");
		}
		
		return "/admin/systemMain";
	}
	
	//회원정보 관리
	@GetMapping("/admin/systemMemberInfo")
	public void getSystemMeberInfo(@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<MemberEntity> members;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			members = service.searchMembers(pageNum, postNum, searchType, searchKeyword);
		} else {
			members = service.memberAllList(pageNum, postNum);
		}
	
		PageUtil page = new PageUtil();
		int totalCount = (int) members.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("members", members);
	    model.addAttribute("searchType", searchType);
	    model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("pageList", page.getAdminMemberPageList(pageNum, postNum, pageListCount, totalCount, searchType, searchKeyword));
	}
	
	//문제 리스트
	@GetMapping("/admin/systemTest")
	public void getSystemTest(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<TestDTO> tests;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			tests = service.searchtestListByTitle(pageNum, postNum, searchKeyword);
		} else {
			tests = service.testAllList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) tests.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("tests", tests);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemTest", pageNum, postNum, pageListCount, totalCount, searchKeyword));
		
	}
	
	//채팅방 관리
	@GetMapping("/admin/systemChat")
	public void getSystemChat(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<ChatEntity> chats;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			chats = service.searchChatListByTitle(pageNum, postNum, searchKeyword);
		} else {
			chats = service.chatList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) chats.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("chats",chats);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemChat", pageNum, postNum, pageListCount, totalCount, searchKeyword));
	}
	
	//채팅인원 0인 채팅방 삭제 
	@Transactional
	@DeleteMapping("/admin/systemChatDelete")
    public ResponseEntity<String> deleteEmptyChat() {
        try {
            service.deleteEmptyChats();
            return ResponseEntity.ok("인원 없는 채팅방이 정리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("채팅방 정리에 실패했습니다.");
        }
    }
	
	//공지 관리
	@GetMapping("/admin/systemNotice")
	public void getSystemNotice(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<BoardEntity> boards;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			boards = service.searchNoticeListByTitle(pageNum, postNum, searchKeyword);
		} else {
			boards = service.noticeboardList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("boards",boards);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemNotice", pageNum, postNum, pageListCount, totalCount, searchKeyword));

	}
	
	//공지사항 수정화면
	@GetMapping("/admin/noticeboardModify")
	public String noticeModify(@RequestParam("id") Long id, Model model) {
		try {
			BoardDTO boardDTO = service.getNoticeById(id);
			model.addAttribute("board", boardDTO);
			
			return "/admin/noticeboardModify";
		} catch (Exception e) {
	        log.error("Error during noticeModify", e);
	        return "{\"message\": \"fail\"}";
	    }
	}
	
	//공지사항 수정
	@ResponseBody
	@PostMapping("/admin/noticeboardModify")
	public String noticeModify(@ModelAttribute BoardDTO boardDTO) {
		try {
			
			service.savenoticeModify(boardDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during noticeModify", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	
	//공지사항 등록
	@ResponseBody
	@PostMapping("/admin/noticeWrite")
	public String noticeWrite(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}	
	
	//게시물 삭제
	@Transactional
	@DeleteMapping("/admin/systemBoardDelete/{idx}") 
	public ResponseEntity<String> getBoardDelete(@PathVariable("idx") Long idx) {
		try {
	        service.deleteBoard(idx);
	        return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
	    }
	}

	//자유게시판 관리
	@GetMapping("/admin/systemFreeBoard")
	public void getSystemFreeBoard(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<BoardDTO> boards;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
				boards = service.searchFreeboardListByTitle(pageNum, postNum, searchKeyword);
		} else {
				boards = service.freeboardList(pageNum, postNum);
		}
		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("boards", boards);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemFreeBoard", pageNum, postNum, pageListCount, totalCount, searchKeyword));
	}
	
	//질문게시판 관리
	@GetMapping("/admin/systemQBoard")
	public void getSystemQBoard(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<TestQuestionEntity> questions;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			questions = service.searchQboardListByTitle(pageNum, postNum, searchKeyword);
		} else {
			questions = service.questionList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) questions.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("questions", questions);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemQBoard", pageNum, postNum, pageListCount, totalCount, searchKeyword));
	}
	
	//질문게시판 글 삭제
	@Transactional
	@DeleteMapping("/admin/systemQBoardDelete/{idx}") 
	public ResponseEntity<String> getQBoardDelete(@PathVariable("idx") Long idx) {
		try {
			service.deleteQBoard(idx);
			return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
		}
	}
		
	//댓글 관리
	@GetMapping("/admin/systemReply")
	public void getSystemReply(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<ReplyDTO> replys;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			replys = service.searchReplyListByContent(pageNum, postNum, searchKeyword);
		} else {
			replys = service.replyList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) replys.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("replys", replys);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemReply", pageNum, postNum, pageListCount, totalCount, searchKeyword));

	}
	
	//댓글 삭제
	@DeleteMapping("/admin/systemReplyDelete/{idx}") 
	public ResponseEntity<String> getReplyDelete(@PathVariable("idx") Long idx) {
		try {
			service.deleteReply(idx);
			return ResponseEntity.ok("댓글이 정상적으로 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
		}
	}
	
	//공지작성화면
	@GetMapping("/admin/noticeboardWrite")
	public void getNoticeboardWrite() { }
	
	//문제 작성화면
	@GetMapping("/admin/testboardWrite")
	public void getTestboardWrite() { }
	
	//문제 작성
	@ResponseBody
	@PostMapping("/admin/testboardWrite")
	public String testWrite(@RequestBody TestDTO testDTO) {
		try {
			service.saveTestWrite(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	//문제 수정화면
	@GetMapping("/admin/testboardModify")
	public String modifyTest(@RequestParam("id") Long id, Model model) {
		try {
			
			TestDTO testDTO = service.getTestById(id); // 서비스에서 데이터 조회
			model.addAttribute("test", testDTO);
			
			List<String> diffList = List.of("0", "1", "2");
			model.addAttribute("diffList", diffList);
			
			
			return "/admin/testboardModify";
		} catch (Exception e) {
			log.error("Error during modifyTest", e);
			return "{\"message\": \"fail\"}";
		}
	}
	//문제 수정
	@ResponseBody
	@PostMapping("/admin/testboardModify")
	public String modifyTest(@RequestBody TestDTO testDTO) {
		try {
			
			service.saveTestModify(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	//회원 탈퇴
	@ResponseBody
	@PostMapping("/admin/systemMemberDelete/{email}")
	public String deleteMember(@PathVariable("email") String email) {

		try {
			service.deleteMember(email);
			return "{ \"message\": \"good\" }";
		} catch (Exception e) {
			return "{\"message\": \"fail\"}";
		}
	}
	/*
	 //질문게시판 관리
	@GetMapping("/admin/systemQBoard")
	public void getSystemQBoard(@RequestParam(required = false) String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<TestQuestionEntity> questions;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			questions = service.searchQboardListByTitle(pageNum, postNum, searchKeyword);
		} else {
			questions = service.questionList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) questions.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("questions", questions);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemQBoard", pageNum, postNum, pageListCount, totalCount, searchKeyword));
	}
	 */
	//공통코드 관리
	@GetMapping("/admin/systemCommonCode")
	public void getCommonCode(@RequestParam(required = false) String searchKeyword,@RequestParam(required = false, defaultValue = "ALL") String filter, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<CommonCodeEntity> codes;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			codes = service.searchCodeListByCode(pageNum, postNum, searchKeyword);
			filter = "";
		} else if (!"ALL".equalsIgnoreCase(filter)) {
			codes= service.getCodeListByType(pageNum, postNum,filter);
		} else {
			codes = service.codeList(pageNum, postNum);
		}
		
		PageUtil page = new PageUtil();
		int totalCount = (int) codes.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("codes", codes);
		model.addAttribute("filter", filter);
		model.addAttribute("pageList", page.getPageListKeyword("/admin/systemCommonCode", pageNum, postNum, pageListCount, totalCount, searchKeyword));

	}
	
	//공통코드 작성 화면
	@GetMapping("/admin/systemCommonCodeWrite")
	public void getCommonCodeWrite() {
		
	}
	
	//공통코드 추가
	@ResponseBody
	@PostMapping("/admin/systemCommonCodeWrite")
	public String CommonCodeWrite(CommonCodeDTO code) throws Exception {
		service.codewrite(code);
		return "{\"message\":\"good\"}";
	}
	
	//공통코드 삭제
	@ResponseBody
	@PostMapping("/admin/systemCommonCodeDelete")
	public void deleteCommonCode(@RequestParam("code") String code) {
		service.deleteCommonCode(code);
	}
}

