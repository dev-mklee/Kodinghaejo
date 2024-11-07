package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.service.MasterService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@AllArgsConstructor
@Log4j2
public class MasterController {
	
	private final MasterService masterService;
	
    private TestRepository testRepository;
	
	@GetMapping("/page-admin/systemMain")
	public void getSystemMain() {
		
	}
	
	@GetMapping("/page-admin/systemMemberInfo")
	public void getSystemMeberInfo() {
		
	}
	
	@GetMapping("/page-admin/systemTest")
	public String getSystemTest(Model model) {
		List<TestDTO> tests = masterService.testAllList(); // 문제 리스트
        model.addAttribute("tests", tests);
        
        long testCount = testRepository.count();
        model.addAttribute("testCount", testCount);
        
        return "/page-admin/systemTest"; // 템플릿 파일 이름
	}
	
	@GetMapping("/page-admin/systemChat")
	public void getSystemChat() {
		
	}
	
	@GetMapping("/page-admin/systemNotice")
	public void getSystemNotice() {
		
	}
	
	@GetMapping("/page-admin/systemFreeBoard")
	public void getSystemFreeBoard() {
		
	}
	@GetMapping("/page-admin/systemQBoard")
	public void getSystemQBoard() {
		
	}
	@GetMapping("/page-admin/systemReply")
	public void getSystemReply() {
		
	}
	@GetMapping("/page-admin/noticeboardWrite")
	public void getNoticeboardWrite() {
		
	}
	@GetMapping("/page-admin/testboardWrite")
	public void getTestboardWrite() {
		
	}
	@ResponseBody
	@PostMapping("/page-admin/testboardWrite")
	public String testWrite(@RequestBody TestDTO testDTO) {
		try {
			masterService.saveTestWrite(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}
}