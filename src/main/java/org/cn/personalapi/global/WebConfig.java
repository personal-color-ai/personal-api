package org.cn.personalapi.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 어떤 URL 패턴에 대해 허용할지
                .allowedOrigins("http://localhost:5173")                            // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")          // 허용할 HTTP 메서드
                .allowedHeaders("*")                                               // 요청 헤더
                .allowCredentials(true)                                            // 쿠키/인증정보 허용 여부
                .maxAge(3600);                                                     // preflight 결과 캐싱 시간(초)
    }
}