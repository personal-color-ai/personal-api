package org.cn.personalapi.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${service.fast-api.url}")
    private String fastAPIUrl;

    @Value("${service.llm-api.url}")
    private String llmAPIUrl;

    @Value("${service.llm-api.key}")
    private String apiKey;

    // FastAPI 전용 WebClient 빈 등록
    @Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
            .baseUrl(fastAPIUrl)
            .build();
    }

    // 다른 외부 API 전용 WebClient 빈 등록
    @Bean
    public WebClient externalWebClient() {
        return WebClient.builder()
            .baseUrl(llmAPIUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .build();
    }
}
