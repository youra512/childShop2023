package com.project.childShop.vo;

import java.sql.Date;

import lombok.Data;


@Data
public class ProductVO2 {

	/**상품 ID*/
	private int productId;
	
	/**상품 이미지*/
	private String productImage;
	
	/**썸네일 이미지*/
	private String productThumbNail;
	
	/**상품명*/
	private String productName;
	
	/**상품 가격*/
	private String productPrice;
	
	/**브랜드*/
	private String productBrand;
	
	/**상품소개(이미지)*/
	private String productDetail;
	
	/**상품코드(고유번호)*/
	private String productUniId;
	
	/**등록날짜*/
	private Date productRegdate;
	
	
}
