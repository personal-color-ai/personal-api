package org.cn.personalapi.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.product.dto.CrawlingDto;
import org.cn.personalapi.domain.product.dto.EmbedDto;
import org.cn.personalapi.domain.review.domain.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class FastAppUtil {
    private final ReviewRepository reviewRepository;

    @Value("${service.fast-api.url}")
    private String fastUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper om;


    public List<FastDto.Beauty> crawlingBeauty(CrawlingDto.BeautyReq dto) {

        // url 설정
        String url = UriComponentsBuilder
                .fromUriString(fastUrl + "/crawling/musinsa/beauty")
                .queryParam("category", dto.category().getCode()) // 값 명시
                .queryParam("page", 1)
                .queryParam("size", 60)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Beauty> apiResponse;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {
            });

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
            .fromUriString(fastUrl + "/crawling/musinsa/reviews")
            .queryParam("goods_no", goodsNo)
            .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Review> apiResponse;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {
            });

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


    public List<FastDto.Option> crawlingOption(String goodsNo) {

        // url 설정
        String url = UriComponentsBuilder
            .fromUriString(fastUrl + "/crawling/musinsa/options")
            .queryParam("goods_no", goodsNo)
            .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String jsonBody = responseEntity.getBody();

        List<FastDto.Option> apiResponse;

        try {
            apiResponse = om.readValue(jsonBody, new TypeReference<>() {
            });

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

    public EmbedDto.FastEmbeddingResponse embedBeauty(List<EmbedDto.ProductDTO> dto) {
        long startTime = System.currentTimeMillis();

        // url 설정
        String url = UriComponentsBuilder
                .fromUriString(fastUrl + "/embedding")
                .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 생성
        HttpEntity<List<EmbedDto.ProductDTO>> request = new HttpEntity<>(dto,headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String jsonBody = responseEntity.getBody();

        EmbedDto.FastEmbeddingResponse apiResponse;

        try {
            apiResponse = om.readValue(jsonBody, EmbedDto.FastEmbeddingResponse.class);

            if (apiResponse == null) {
                log.error("FastAPI 요청 실패 응답 Body: {}", jsonBody);
                throw new RuntimeException("FastAPI 요청이 성공했으나, 상품 임베딩 실패.");
            }

        } catch (Exception e) {
            log.error("FastAPI 응답 JSON 파싱에 실패했습니다. Body: {}", jsonBody, e);
            throw new RuntimeException("FastAPI 응답 파싱 실패", e);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("FastAPI 임베딩 API 호출 총 소요 시간: {}ms", (endTime - startTime));
        }
        return apiResponse;
    }

    // 상품 검색 요청
    public List<Long> searchProducts(String personalColor, String prompt) {
        long startTime = System.currentTimeMillis();

        // url 설정 (파이썬 api 경로)
        String url = UriComponentsBuilder
                .fromUriString(fastUrl + "/embedding/search")
                .toUriString();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 생성
        EmbedDto.SearchReq req = new EmbedDto.SearchReq(personalColor, prompt);
        HttpEntity<EmbedDto.SearchReq> request = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
            String jsonBody = responseEntity.getBody();

            EmbedDto.SearchRes apiResponse = om.readValue(jsonBody, EmbedDto.SearchRes.class);

            if (apiResponse == null || apiResponse.results() == null) {
                return Collections.emptyList();
            }

            // 결과에서 Product ID만 추출하여 반환
            return apiResponse.results().stream()
                    .map(EmbedDto.SearchResultItem::productId)
                    .toList();

        } catch (Exception e) {
            log.error("FastAPI 검색 요청 실패. Prompt: {}, Error: {}", prompt, e.getMessage());
            // 검색 실패 시 빈 리스트 반환 (또는 예외를 던져서 상위에서 처리)
            return Collections.emptyList();
        } finally {
            log.info("FastAPI 검색 소요 시간: {}ms", (System.currentTimeMillis() - startTime));
        }
    }
}
