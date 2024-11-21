package com.kodinghaejo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kodinghaejo.entity.BannerEntity;
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
		
		List<BannerEntity> banners;
		banners = adminservice.getBanner();
		
		Random random = new Random();
		BannerEntity randomBanner = banners.get(random.nextInt(banners.size()));
		
		model.addAttribute("banners", randomBanner);
		
		return "index";
	}
	
	@GetMapping("/rank/rank")
	public void getRank() { }

}