package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.service.AdminService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
	@GetMapping("/admin/systemReply")
	public String getSystemReply(Model model) {
		List<ReplyDTO> replyDTOs = service.replyList();
		model.addAttribute("replys", replyDTOs);
		
		long replyCount = replyDTOs.size();
		model.addAttribute("replyCount", replyCount);
		
		return "/admin/systemReply";
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

}