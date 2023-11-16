package com.project.childShop.vo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name="product_tbl")
@Data
public class ProductVO {

	/**상품 ID*/
	@Id
	@GeneratedValue
	@Column(name = "product_id")
	private int productId;
	
	/**상품 이미지*/
	@Column(name = "product_image")
	private String productImage;
	
	/**썸네일 이미지*/
	@Column(name = "product_thumnail")
	private String productThumbNail;
	
	/**상품명*/
	@Column(name = "product_name")
	private String productName;
	
	/**상품 가격*/
	@Column(name = "product_price")
	private String productPrice;
	
	/**브랜드*/
	@Column(name = "product_brand")
	private String productBrand;
	
	/**상품소개(이미지)*/
	@Column(name = "product_detail")
	private String productDetail;
	
	/**상품코드(고유번호)*/
	@Column(name = "product_uniid")
	private String productUniId;
	
	/**등록날짜*/
	@Column(name = "product_regdate")
	@CreationTimestamp // 작성 날짜(기본값) 생성
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // 10.26 (2) JSON 변환시 "년월일 및 시분초"까지 모두 출력
	private Date productRegdate;
	
	
}
