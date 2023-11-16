package com.project.childShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.childShop.service.ProductService;
import com.project.childShop.vo.PageVO;
import com.project.childShop.vo.ProductVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/productList")
	public String productList(@RequestParam(value="page",defaultValue = "1") int page, Model model) {
		
		//총 상품 수
		int listCount = productService.countAll();
		
		//page에 따른 상품정보
		List<ProductVO> list = productService.getProductsByPaging(page, 20);
		
		// 총 페이지 수
   		// int maxPage=(int)((double)listCount/20+0.95); //0.95를 더해서 올림 처리
		int maxPage = PageVO.getMaxPage(listCount, 20);
		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,...)
   		// int startPage = (((int) ((double)page / 10 + 0.9)) - 1) * 10 + 1;
		int startPage = 1;
		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30, ...)
   	    int endPage = maxPage;
   	    
   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(page);
		pageVO.setStartPage(startPage);
		
		// 10.11 (추가)
		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
	
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("list", list);
		
		return "list";
		
	}

}
