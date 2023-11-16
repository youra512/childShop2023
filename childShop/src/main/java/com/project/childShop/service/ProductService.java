package com.project.childShop.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.childShop.dao.ProductDAO;
import com.project.childShop.vo.ProductVO;

@Service
public class ProductService {
	
	@Autowired
	ProductDAO productDao;
	
	@Transactional
	public ProductVO insert(ProductVO productVo) {
		
		return productDao.save(productVo);
	}
	
	@Transactional
	public boolean isBlankProduct(int rownum) {
		
		return productDao.isBlankProduct(rownum) == 1 ? true : false;
	}
	
	// 11.8 (수업시간)
	// 임의 상품 rownum(레코드 번호) n개 추출
	private Set<Integer> getRandomProductsByLimit(int limit) {
		
		Set<Integer> set = new HashSet<>();
		
		for (int i = 0; i <100; i++) { // 11.8
			
			int num = (int) (Math.random()*100);
			
			// 썸네일이 mall.gif 라면 배제하고 그렇지 않으면 추가 : 유효 상품만 추가 : 11.8
			if (this.isBlankProduct(num) == false) {			
				set.add(num);
			}
			
			if (set.size() == limit)
				break;
		}
		
		return set;
	}

	@Transactional(readOnly = true)
	public List<ProductVO> getMainProducts(){
		
		Set<Integer> set = this.getRandomProductsByLimit(5); // 11.8		
		
		Integer[] intArr =new Integer[5];
		intArr = set.toArray(intArr);
		
		return productDao.findRandomProducts(intArr[0],intArr[1],intArr[2],intArr[3],intArr[4]);
	}
	
	// 11.8 (수업시간)
	@Transactional(readOnly = true)
	public List<ProductVO> getMainGalleryProducts(int limit){
		
		Set<Integer> set = this.getRandomProductsByLimit(limit);		
		
		Integer[] intArr = new Integer[limit];
		intArr = set.toArray(intArr);
		
		List<ProductVO> list = new ArrayList<>();
		
		for (int i=0; i<limit; i++) {
			
			ProductVO p = productDao.findRandomProduct(intArr[i]);
			list.add(p);
		}
		
		return list;
	}

	@Transactional(readOnly = true)
	public int countAll() {
		
		return (int)productDao.count();
				
	}
	
	@Transactional(readOnly = true)
	public List<ProductVO> getProductsByPaging(int page, int limit){
		Pageable pageable = PageRequest.of(page -1, limit);
		return productDao.findAll(pageable).getContent();
	}
	
}