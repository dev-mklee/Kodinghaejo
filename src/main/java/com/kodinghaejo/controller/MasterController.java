package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Controller
@AllArgsConstructor
@Log4j2
public class MasterController {
	
	@GetMapping("/system")
	public void getSystem() {
		
	}
	
	@GetMapping("/systemMemberInfo")
	public void getSystemMeberInfo() {
		
	}
	
	@GetMapping("/systemTest")
	public void getSystemTest() {
		
	}
	
	@GetMapping("/systemChat")
	public void getSystemChat() {
		
	}
	
	@GetMapping("/systemNotice")
	public void getSystemNotice() {
		
	}
	
	@GetMapping("/systemFreeBoard")
	public void getSystemFreeBoard() {
		
	}
	@GetMapping("/systemQBoard")
	public void getSystemQBoard() {
		
	}
	@GetMapping("/systemReply")
	public void getSystemReply() {
		
	}
	@GetMapping("/noticeboardWrite")
	public void getNoticeboardWrite() {
		
	}
}
