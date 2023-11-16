package com.project.childShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.childShop.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/")
	public String home(Model model) {
		
		// 메인 슬라이드
		model.addAttribute("mainProducts", productService.getMainProducts());
		
		// 11.8 (수업시간)
		// 갤러리 : 4 * 5 = 20
		model.addAttribute("galleryProducts", productService.getMainGalleryProducts(20));
		
		return "index";
	}
	
	@GetMapping("/content1")
	public String content1() {
		
		log.info("content1");
		
		return "content1";
	} //
	
	@GetMapping("/content2")
	public String content2() {
		
		log.info("content2");
		
		return "content2";
	} //


}
