package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.service.AdminService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@AllArgsConstructor
@Log4j2
public class AdminController {
	
	private final AdminService adminService;
	
    private TestRepository testRepository;
	
	@GetMapping("/admin/systemMain")
	public void getSystemMain() {
		
	}
	
	@GetMapping("/admin/systemMemberInfo")
	public void getSystemMeberInfo() {
		
	}
	
	//문제 리스트
	@GetMapping("/admin/systemTest")
	public String getSystemTest(Model model) {

		List<TestDTO> tests = adminService.testAllList(); // 문제 리스트
        model.addAttribute("tests", tests);
        
        long testCount = testRepository.count();
        model.addAttribute("testCount", testCount);
        
        return "/admin/systemTest"; // 템플릿 파일 이름
	}
	
	@GetMapping("/admin/systemChat")
	public void getSystemChat() {
		
	}
	
	@GetMapping("/admin/systemNotice")
	public void getSystemNotice() {
		
	}
	
	@GetMapping("/admin/systemFreeBoard")
	public void getSystemFreeBoard() {
		
	}
	@GetMapping("/admin/systemQBoard")
	public void getSystemQBoard() {
		
	}
	@GetMapping("/admin/systemReply")
	public void getSystemReply() {
		
	}
	@GetMapping("/admin/noticeboardWrite")
	public void getNoticeboardWrite() {
		
	}
	@GetMapping("/admin/testboardWrite")
	public void getTestboardWrite() {
		
	}
	//문제 작성
	@ResponseBody
	@PostMapping("/admin/testboardWrite")
	public String testWrite(@RequestBody TestDTO testDTO) {
		try {
			adminService.saveTestWrite(testDTO);
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
			
	        TestDTO testDTO = adminService.getTestById(id); // 서비스에서 데이터 조회
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
			
			adminService.saveTestModify(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}
}
