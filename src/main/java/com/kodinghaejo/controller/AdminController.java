package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.service.AdminService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.asm.Advice.Return;

@Controller
@AllArgsConstructor
@Log4j2
public class AdminController {
	
	private final AdminService service;
	private TestRepository testRepository;
	private BoardRepository boardRepository;
	
	@GetMapping("/admin/systemMain")
	public void getSystemMain() {
		
	}
	
	@GetMapping("/admin/systemMemberInfo")
	public void getSystemMeberInfo() {
		
	}
	
	@GetMapping("/admin/systemTest")
	public String getSystemTest(Model model) {
		List<TestDTO> tests = service.testAllList(); // 문제 리스트
		model.addAttribute("tests", tests);
		
		long testCount = testRepository.count();
		model.addAttribute("testCount", testCount);
		
		return "/admin/systemTest"; // 템플릿 파일 이름
	}
	
	@GetMapping("/admin/systemChat")
	public void getSystemChat() {
		
	}
	
	@GetMapping("/admin/systemNotice")
	public String getSystemNotice(Model model) {
		List<BoardDTO> boardDTOs = service.noticeboardList();
		model.addAttribute("boards",boardDTOs);
		
		long boardCount = boardDTOs.size();
		model.addAttribute("boardCount", boardCount);
		

		return "/admin/systemNotice";
	}
	
	//게시물 등록
	@ResponseBody
	@PostMapping("/admin/noticeWrite")
	public String noticeWrite(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}	
	
	
	@DeleteMapping("/admin/systemBoardDelete/{idx}") 
	public ResponseEntity<Void> getBoardDelete(@PathVariable("idx") Long idx) {
		service.deleteBoard(idx);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/admin/systemFreeBoard")
	public String getSystemFreeBoard(Model model) {
		List<BoardDTO> boardDTOs = service.freeboardList();
		model.addAttribute("boards",boardDTOs);
		
		long boardCount = boardDTOs.size();
		model.addAttribute("boardCount", boardCount);
		

		return "/admin/systemFreeBoard";
	}
	
	@GetMapping("/admin/systemQBoard")
	public String getSystemQBoard(Model model) {
		List<TestQuestionDTO> questionDTOs = service.questionList();
		model.addAttribute("questions", questionDTOs);
		
		long questionCount = questionDTOs.size();
		model.addAttribute("questionCount", questionCount);
		
		return "/admin/systemQBoard";
	}
	
	@DeleteMapping("/admin/systemQBoardDelete/{idx}") 
	public ResponseEntity<Void> getQBoardDelete(@PathVariable("idx") Long idx) {
		service.deleteQBoard(idx);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/admin/systemReply")
	public String getSystemReply(Model model) {
		List<ReplyDTO> replyDTOs = service.replyList();
		model.addAttribute("replys", replyDTOs);
		
		long replyCount = replyDTOs.size();
		model.addAttribute("replyCount", replyCount);
		
		return "/admin/systemReply";
	}
	
	@DeleteMapping("/admin/systemReplyDelete/{idx}") 
	public ResponseEntity<Void> getReplyDelete(@PathVariable("idx") Long idx) {
		service.deleteReply(idx);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/admin/noticeboardWrite")
	public void getNoticeboardWrite() {
		
	}
	@GetMapping("/admin/testboardWrite")
	public void getTestboardWrite() {
		
	}
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
	
//	@GetMapping("/boardList")
//	public String getBoardList(@RequestParam(defaultValue = "1") int page, Model model) {
//	    int pageSize = 10;  // 페이지 당 게시글 수
//	    int offset = (page - 1) * pageSize; // 시작 인덱스 계산
//	    int totalCount = boardRepository.countAllBoards(); // 총 게시글 수
//	    List<BoardEntity> boards = boardRepository.findBoards(offset, pageSize); // 해당 페이지의 게시글 조회
//
//	    // 총 페이지 수 계산
//	    int totalPages = (int) Math.ceil((double) totalCount / pageSize);
//	    
//	    model.addAttribute("boards", boards);
//	    model.addAttribute("totalPages", totalPages);
//	    model.addAttribute("currentPage", page);
//	    
//	    return "boardList";  // Thymeleaf HTML 템플릿
//	}
	
}