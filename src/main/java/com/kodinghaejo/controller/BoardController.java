package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BoardController {

	@GetMapping("/board")
	public void getBoard() {
		
	}
	
	@GetMapping("/mypagemain")
	public void getMypagemain() {
		
	}
	@GetMapping("/mypageMyboard")
	public void getMypageMyboard() {
		
	}
	@GetMapping("/mypageMychat")
	public void getMypageMychat() {
		
	}
	@GetMapping("/mypageMytest")
	public void getMypageMytest() {
		
	}
	
	@GetMapping("/testboardWrite")
	public void getTestboardWrite() {
		
	}

}