package com.project.childShop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
			// .setCachePeriod(20); 
		
//		registry.addResourceHandler("/jquery/**")
//				.addResourceLocations("classpath:/META-INF/resources/webjars/jquery/");
				
		registry.addResourceHandler("/bootstrap/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/");
		
		registry.addResourceHandler("/bootstrap-icons/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap-icons/");
		
		// 추가 : 상품 이미지 경로
		registry.addResourceHandler("/product_image/**")
				.addResourceLocations("file:///C:/BLUE/projectChild/image/");
		
	}
	
}