package com.project.childShop;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CategoryCrawlTest {
	
	@Test
	public void crawlTest() throws IOException {
	
		// 보육사 > 시설 교구
		String toySiteCateLink = "https://www.boyuksa.co.kr/product/list.html?cate_no=169";
		Document doc = Jsoup.connect(toySiteCateLink).get();
		
		// page 구조
		// : 페이지 소스 열었을 경우 사이트 주석 설명이 정말(!) 잘 되어 있음 !
		// 
		// <div id="contents">
		//    ....
		// 		<a href="/product/......." name="anchorBoxName_11151">
		//         ....			
		// 		<div class="d_thumb">
     	// 			...
		//        
		
		// log.info("카테고리 html 내용 : " + doc.select("div#contents").get(0).html()); // 실질적 최상단
		// log.info("카테고리 html 내용 : " + doc.select("div#contents a[name^='anchorBoxName_']").get(0).html()); // 개별 상품 패널
		
		log.info("카테고리 개별 상품 갯수 : " + doc.select("div#contents a[name^='anchorBoxName_']").size()); // 개별 상품 갯수 : 20개 (페이지당 20개 상품)
		
		// 개별 상품 아이디 ex) 11151
		Elements elements = doc.select("div#contents a[name^='anchorBoxName_']");
		
		// <a href="/product/detail.html?product_no=11151&amp;cate_no=169&amp;display_group=1" name="anchorBoxName_11151">		
		log.info("개별 상품 아이디 : {}" , elements.get(0).attr("name").substring("anchorBoxName_".length())); // 11151
		
		List<String> productIdList = new ArrayList<>();
		
		// 1 페이지에 수록된 개별 상품 아이디들
		for (Element element : elements) {
			
			String id = element.attr("name").substring("anchorBoxName_".length());
			productIdList.add(id);
		}
		
		// 20개의 상품 아이디들
		// productIdList.forEach(x-> { log.info("상품 아이디 : " + x ); } );
		
		log.info("------------------------------------------------------------------------");
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//
		// 개별 상품 정보 추출
		
		// 개별 상품 링크 
		// https://www.boyuksa.co.kr/product/detail.html?product_no=11151&cate_no=169&display_group=1
		
		String toySiteProductLink = "https://www.boyuksa.co.kr/product/detail.html?product_no=11151&cate_no=169&display_group=1";
		Document docProduct = Jsoup.connect(toySiteProductLink).get();
		
		// 정적 페이지 소스 보기
		/*
			<div class="detailArea ">
			        
			        <!-- 이미지영역 -->
			        <div class="xans-element- xans-product xans-product-image imgArea "><div class="keyImg item">
			                <!-- 꾸미기 아이콘 -->
			                                
			                <!-- 좋아요 버튼 
			                <div class="-likeButton -mov likePrd likePrd_11151">
			                    <button type="button" class="-mov"><img src="/web/upload/icon_202302071833084900.png"  class="likePrdIcon" product_no="11151" category_no="169" icon_status="off" alt="좋아요 등록 전" /><span class="count"><span class="likePrdCount likePrdCount_11151">2</span></span></button>
			                </div>-->
			                
			                <!-- 상품이미지 -->
			        		<div id="big_img_box" data-one_big_img="//www.boyuksa.co.kr/web/product/big/202302/ea947218cdb3a7599f2de2e1292b1729.jpg"></div>
			            </div>
			<div class="xans-element- xans-product xans-product-addimage listImg"><ul>
			<li class="xans-record-"><img src="//www.boyuksa.co.kr/web/product/small/202302/43e0a69ad9f79e0b153e20e7f30ecce5.jpg"  class="ThumbImage" alt="" /></li>
			                    <li class="xans-record-"><img src="//www.boyuksa.co.kr/web/product/extra/small/202302/a42e0485060fed101209558a35dd069a.jpg"  class="ThumbImage" alt="" /></li>
			                </ul>
			</div>

		 */
		
		/*
		   <!-- 상품이미지 -->
    	   <div id="big_img_box" data-one_big_img="//www.boyuksa.co.kr/web/product/big/202302/ea947218cdb3a7599f2de2e1292b1729.jpg"></div> 
		 */
		// 상품 이미지 주소 추출 (큰 그림)
		log.info("개별 상품 이미지  : {}", docProduct.select("div[class='detailArea '] div#big_img_box").attr("data-one_big_img"));

		// 상품 이미지 주소 추출 (작은 그림 : thumbnail)
		/*
		 <div class="xans-element- xans-product xans-product-addimage listImg"><ul>
			<li class="xans-record-"><img src="//www.boyuksa.co.kr/web/product/small/202302/43e0a69ad9f79e0b153e20e7f30ecce5.jpg"  class="ThumbImage" alt="" /></li>
			                    <li class="xans-record-"><img src="//www.boyuksa.co.kr/web/product/extra/small/202302/a42e0485060fed101209558a35dd069a.jpg"  class="ThumbImage" alt="" /></li>
			                </ul>
		 </div> 
		 */
		log.info("개별 상품 이미지(썸네일)-1  : {}", docProduct.select("li[class='xans-record-'] img[class='ThumbImage']").get(0).attr("src"));
		
		//  <li class="xans-record-"><img src="//www.boyuksa.co.kr/web/product/extra/small/202302/a42e0485060fed101209558a35dd069a.jpg"  class="ThumbImage" alt="" /></li>
		log.info("개별 상품 이미지(썸네일)-2  : {}", docProduct.select("li[class='xans-record-'] img[class='ThumbImage']").get(1).attr("src"));
		
		// 상품명 추출
		log.info("상품명 : {}", docProduct.select("h2[class='item_name']").get(0).html());
		
		// 판매기 추출
		log.info("판매가 : {}", docProduct.select("strong[id='span_product_price_text']").get(0).html().replaceAll("원", "").replaceAll(",", ""));
		
		// TODO : 다른 상품 정보 추출
						
		log.info("-----------------------------------------------------------------------------");
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 
		// 이미지 다운로드 : 낱개의 이미지
		// 
		String targetImagePath = "C:/downloads/product_images/";
		
		// 이미지 다운로드 복사
		// 메인 이미지 다운로드
		// log.info("개별 상품 이미지  : {}", docProduct.select("div[class='detailArea '] div#big_img_box").attr("data-one_big_img"));
		
		String imgURL = docProduct.select("div[class='detailArea '] div#big_img_box").attr("data-one_big_img");
		String imgName = imgURL.substring(imgURL.lastIndexOf('/') + 1); // 마지막 "/"의 index 추출 => 파일명만 추출됨
		String extName = imgName.substring(imgName.lastIndexOf('.') + 1); // 파일 확장자 
		String imgEncodedName = UUID.randomUUID().toString() + "." + extName;
		
		log.info("그림 URL : {}", imgURL);
		log.info("그림 원 파일명  : {}", imgName);
		log.info("그림 확장자명  : {}", extName);
		log.info("그림 자체 암호화 파일명 : {}", imgEncodedName);
		
		// 원 파일명 저장 : 그림 찾기의 용이성을 위해 상품명으로 저장
		
		// 상품명
		String productName = docProduct.select("h2[class='item_name']").get(0).html().trim();
		// 파일 저장명-1 : 상품명.jpg
		String imgSaveName = productName + "." + extName;
		
		/*		
		   - 변경전 : //www.boyuksa.co.kr/web/product/extra/small/202302/a42e0485060fed101209558a35dd069a.jpg 
	       - 변경후 : https://www.boyuksa.co.kr/web/product/extra/small/202302/a42e0485060fed101209558a35dd069a.jpg
		 */
		imgURL = imgURL.replaceAll("//", "https://"); 
		InputStream in = new URL(imgURL).openStream();
		Files.copy(in, Paths.get(targetImagePath + imgSaveName), StandardCopyOption.REPLACE_EXISTING);
		
		// 자체 암호화 파일명 저장
		in = new URL(imgURL).openStream();
		Files.copy(in, Paths.get(targetImagePath + imgEncodedName), StandardCopyOption.REPLACE_EXISTING);
		
		in.close();
		
	} //

}
