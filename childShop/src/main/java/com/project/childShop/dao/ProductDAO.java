package com.project.childShop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.project.childShop.vo.ProductVO;

public interface ProductDAO extends PagingAndSortingRepository<ProductVO, Integer>{
	
	// 11.8
	// 크롤링 상품 중 무효 상품 배제
	@Query(value = "SELECT count(decode(p.product_thumnail,'mall.gif','true'))  " + 
					"FROM (SELECT rownum AS row_no, m.*  " + 
					"      FROM (" + 
					"             SELECT * " + 
					"             FROM product_tbl " + 
					"           ) m  " + 
					"      ) p  " + 
					"WHERE p.row_no = :rowNum", nativeQuery = true) 
	public int isBlankProduct(@Param("rowNum") int rowNum);
	
	// 임의의 메인 상품 1개 선택 : 11.8 (수업시간)
	@Query(value = "SELECT p.*  " + 
					"FROM (SELECT rownum AS row_no, m.*  " + 
					"      FROM (" + 
					"             SELECT * " + 
					"             FROM product_tbl            " + 
					"           ) m  " + 
					"      ) p  " + 
					"WHERE p.row_no = :rowNum", nativeQuery = true)
	public ProductVO findRandomProduct(@Param("rowNum") int rowNum);
	
	// 임의의 메인 상품 5개 선택
	@Query(value = "SELECT p.*  " + 
					"FROM (SELECT rownum AS row_no, m.*  " + 
					"      FROM (" + 
					"             SELECT * " + 
					"             FROM product_tbl            " + 
					"           ) m  " + 
					"      ) p  " + 
					"WHERE p.row_no in ( :rowNum1, :rowNum2, :rowNum3, :rowNum4, :rowNum5 )", nativeQuery = true)
	public List<ProductVO> findRandomProducts(@Param("rowNum1") int rowNum1,
												@Param("rowNum2") int rowNum2,
												@Param("rowNum3") int rowNum3,
												@Param("rowNum4") int rowNum4,
												@Param("rowNum5") int rowNum5);
	
	
}
