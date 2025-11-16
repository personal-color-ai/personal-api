package org.cn.personalapi.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class FastAppUtil {

    @Value("${fast-api.url}")
    private String fastUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper om;

    public List<FastDto.Fashion> crawlingFashion() {

        // url 설정
        String url = UriComponentsBuilder
                .fromHttpUrl(fastUrl + "/crawling/musinsa/ranking/all")
                .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Fashion> apiResponse = null;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {});

            if (apiResponse == null) {
                log.error("FastAPI 요청 실패 응답 Body: {}", jsonBody);
                throw new RuntimeException("FastAPI 요청이 성공했으나, 무신사-패션 크롤링 실패.");
            }

        } catch (Exception e) {
            log.error("FastAPI 응답 JSON 파싱에 실패했습니다. Body: {}", jsonBody, e);
            throw new RuntimeException("FastAPI 응답 파싱 실패", e);
        }
        return apiResponse;
    }

    public List<FastDto.Beauty> crawlingBeauty() {

        // url 설정
        String url = UriComponentsBuilder
                .fromHttpUrl(fastUrl + "/crawling/musinsa/beauty")
                .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Beauty> apiResponse = null;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {});

            if (apiResponse == null) {
                log.error("FastAPI 요청 실패 응답 Body: {}", jsonBody);
                throw new RuntimeException("FastAPI 요청이 성공했으나, 무신사-화장품 크롤링 실패.");
            }

        } catch (Exception e) {
            log.error("FastAPI 응답 JSON 파싱에 실패했습니다. Body: {}", jsonBody, e);
            throw new RuntimeException("FastAPI 응답 파싱 실패", e);
        }
        return apiResponse;
    }


    public List<FastDto.Review> crawlingReview(String goodsNo) {

        // url 설정
        String url = UriComponentsBuilder
                .fromHttpUrl(fastUrl + "/crawling/musinsa/reviews")
                .queryParam("goods_no", goodsNo)
                .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Review> apiResponse = null;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {});

            if (apiResponse == null) {
                log.error("FastAPI 요청 실패 응답 Body: {}", jsonBody);
                throw new RuntimeException("FastAPI 요청이 성공했으나, 무신사-화장품 크롤링 실패.");
            }

        } catch (Exception e) {
            log.error("FastAPI 응답 JSON 파싱에 실패했습니다. Body: {}", jsonBody, e);
            throw new RuntimeException("FastAPI 응답 파싱 실패", e);
        }
        return apiResponse;
    }
}
