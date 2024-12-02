package com.tilon.ojt_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000") //내 포트가 아니라 들어오는 url 로 지정해줘야 한다. 예를 들어 요청을 하는 프론트의 url로 지정 
        .allowedMethods("*");
    }

    // 정적 리소스 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**") // "/static/**" 경로로 들어오는 요청을 처리
                .addResourceLocations("classpath:/static/"); // Spring Boot의 기본 정적 리소스 경로 사용
    }
}

