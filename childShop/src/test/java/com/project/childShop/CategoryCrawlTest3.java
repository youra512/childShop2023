package com.project.childShop;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.childShop.service.ProductCrawlService;


@SpringBootTest
public class CategoryCrawlTest3 {
	
	@Autowired
	ProductCrawlService productCrawlService;
	
	@Test
	public void crawlTest() throws IOException {
	
		String url = "https://www.boyuksa.co.kr/product/list.html?cate_no=169&page=1";
		productCrawlService.crawlProducts(url);
		
		url = "https://www.boyuksa.co.kr/product/list.html?cate_no=169&page=2";
		productCrawlService.crawlProducts(url);
		
		url = "https://www.boyuksa.co.kr/product/list.html?cate_no=224&page=1";
		productCrawlService.crawlProducts(url);
		
		url = "https://www.boyuksa.co.kr/product/list.html?cate_no=224&page=2";
		productCrawlService.crawlProducts(url);
		
		url = "https://www.boyuksa.co.kr/product/list.html?cate_no=224&page=3";
		productCrawlService.crawlProducts(url);
		
	}
	
}