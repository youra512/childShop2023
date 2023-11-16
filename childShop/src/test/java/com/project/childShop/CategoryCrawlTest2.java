package com.project.childShop;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.childShop.service.ProductService;
import com.project.childShop.vo.ProductVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CategoryCrawlTest2 {
	
	@Autowired
	ProductService productService;
	
	
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
		
		ProductVO productVO;
		
		InputStream in = null; // 이미지 다운로드를 위한 입력 스트림
		
		// 1 페이지에 수록된 개별 상품 아이디들
		for (Element element : elements) {
			productVO = new ProductVO();
			
			log.info("------");
			
			String id = element.attr("name").substring("anchorBoxName_".length()); // 상품 아이디
			
			log.info("상품 아이디 : {}", id);
			//productIdList.add(id);

			
			///////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 개별 상품 정보 추출
			
			// 개별 상품 링크 
			// https://www.boyuksa.co.kr/product/detail.html?product_no=11151&cate_no=169&display_group=1
			
			// String toySiteProductLink = "https://www.boyuksa.co.kr/product/detail.html?product_no=11151&cate_no=169&display_group=1";
			String toySiteProductLink = "https://www.boyuksa.co.kr/product/detail.html?product_no=" + id + "&cate_no=169&display_group=1";
			Document docProduct = Jsoup.connect(toySiteProductLink).get();
			
			// 상품 이미지 주소 추출 (큰 그림)
			log.info("개별 상품 이미지  : {}", docProduct.select("div[class='detailArea '] div#big_img_box").attr("data-one_big_img"));
			
			// 상품 이미지 주소 추출 (작은 그림 : thumbnail)
			log.info("개별 상품 이미지(썸네일)-1  : {}", docProduct.select("li[class='xans-record-'] img[class='ThumbImage']").get(0).attr("src"));
			// log.info("개별 상품 이미지(썸네일)-2  : {}", docProduct.select("li[class='xans-record-'] img[class='ThumbImage']").get(1).attr("src"));
			
			// 상품명 추출
			log.info("상품명 : {}", docProduct.select("h2[class='item_name']").get(0).html());
			
			
			
			// 판매기 추출
			log.info("판매가 : {}", docProduct.select("strong[id='span_product_price_text']").get(0).html().replaceAll("원", "").replaceAll(",", ""));
			
			// TODO : 다른 상품 정보 추출
			// 브랜드 추출
			/*  
				<tr class=" xans-record-">
					<th scope="row">
						<span style="font-size:12px;color:#555555;">브랜드</span>
					</th>
					<td><span style="font-size:12px;color:#555555;">LAKESHORE</span></td>
				</tr>
			*/
			// 브랜드 제목 패널과 같은 형식의 패널들 : 적립금, 배송비, 브랜드, 상품코드 
			// 브랜드 제목 패널
			// <th scope="row"><span style="font-size:12px;color:#555555;">브랜드</span></th>
			// log.info("브랜드 패널과 유사 패널 갯수 : {}", docProduct.select("tr[class=' xans-record-']").size()); // 총 4개
			
			// 브랜드 제목 패널 -> 같은 부류의 패널중에서 3번째(index:2) => 자녀 태그들(th:get(0), td:get(1)) => span의 값(text()) = 브랜드명
			// <span style="font-size:12px;color:#555555;">LAKESHORE</span>
			log.info("브랜드 패널 : {}", docProduct.select("tr[class=' xans-record-']").get(2).children().get(1).text());
			
			// 상품 코드
			// 브랜드 제목 패널 -> 같은 부류의 패널중에서 4번째(index:3) => 자녀 태그들(th:get(0), td:get(1)) => span의 값(text()) = 상품 코드
			// <span style="font-size:12px;color:#555555;">024114</span>
			log.info("상품 코드 : {}", docProduct.select("tr[class=' xans-record-']").get(3).children().get(1).text());
			
			// 상품 상세 소개
			// ex) /web/upload/NNEditor/20230228/EC8BA0ECA09CED9288_6.jpg
			/*
			 * <div class="cont">
            	<p><br></p>
        
				  <img style="display: block; vertical-align: top; margin: 0px auto; text-align: center; width: 800px;" 
				      ec-data-src="/web/upload/NNEditor/20230228/EC8BA0ECA09CED9288_6.jpg" 
				      result="success" 
				      name="EC8BA0ECA09CED9288_6.jpg" 
				      size="860px/1608px" 
				      filesize="595,49 kB"
				      error="" >
			 */
			
			// ex) /web/upload/NNEditor/20230228/EC8BA0ECA09CED9288_6.jpg
			log.info("상품 상세 내용(이미지) : {}", docProduct.select("div[class='cont'] img").get(0).attr("ec-data-src"));
			
			// 상품 상세 내용(이미지)
			String productDetailImage = docProduct.select("div[class='cont'] img").get(0).attr("ec-data-src");
			// 다운받으려면 앞 부분에 사이트 주소 추가해야 됨.
			// https://www.boyuksa.co.kr/ + productDetailImage 
			
			String targetImagePath = "C:/BLUE/projectChild/image/"; // 이 자리로 옮김 : 11.3			
			
			// 파일명은 "productDetail_" + id 형식  + 확장자
			
			// 확장자 추출
			String detailExtName = productDetailImage.substring(productDetailImage.lastIndexOf(".")+1); // jpg
			
			String productDetailSaveName = "productDetail_" + id + "." + detailExtName; 
			
			//
			// 주의사항) 간혹 https://www.boyuksa.co.kr/edit/upload/201801291128570.jpg 와 같이 되어 있는 상품 아이디도 있음 (011999)
			//
			// 선택적으로 크롤링 !
			// https://www.boyuksa.co.kr/ 포함여부로 조건 결정
			String mainSiteURL = "https://www.boyuksa.co.kr/";
			
			String productDetailImageURL = productDetailImage.contains(mainSiteURL) ? productDetailImage 
										  : mainSiteURL + productDetailImage; 
						
			in = new URL(productDetailImageURL).openStream();
			Files.copy(in, Paths.get(targetImagePath + productDetailSaveName), StandardCopyOption.REPLACE_EXISTING);
			
						
			log.info("-----------------------------------------------------------------------------");
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 
			// 이미지 다운로드 : 낱개의 이미지
			// 
			// String targetImagePath = "C:/downloads/product_images/";
			
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
			in = new URL(imgURL).openStream();
			Files.copy(in, Paths.get(targetImagePath + imgSaveName), StandardCopyOption.REPLACE_EXISTING);
			
			// 자체 암호화 파일명 저장
			in = new URL(imgURL).openStream();
			Files.copy(in, Paths.get(targetImagePath + imgEncodedName), StandardCopyOption.REPLACE_EXISTING);
			
			//in.close();
			
			productVO.setProductName(productName);
			productVO.setProductBrand(docProduct.select("tr[class=' xans-record-']").get(2).children().get(1).text());
			productVO.setProductThumbNail(docProduct.select("li[class='xans-record-'] img[class='ThumbImage']").get(0).attr("src"));
			productVO.setProductDetail(productDetailImageURL);
			productVO.setProductImage(imgEncodedName);
			productVO.setProductPrice(docProduct.select("strong[id='span_product_price_text']").get(0).html().replaceAll("원", "").replaceAll(",", ""));
			productVO.setProductUniId(elements.get(0).attr("name").substring("anchorBoxName_".length()));
			//productList.add(productVO);
			
			
			productService.insert(productVO);
			
			
		} //for
		
		in.close();
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		// 20개의 상품 아이디들
		//productList.forEach(x-> { log.info("상품  : " + x ); } );
		
		log.info("------------------------------------------------------------------------");
		
	} //

}
