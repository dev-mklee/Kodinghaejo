package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kodinghaejo.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BaseController {
	
	private final AdminService adminservice;
	
	@GetMapping({"/", "/index"})
	public String getIndex(Model model, HttpServletRequest request) {
		adminservice.upTodayVisitorCount(request);
		
		return "index";
	}
	
	@GetMapping("/rank/rank")
	public void getRank() { }

}