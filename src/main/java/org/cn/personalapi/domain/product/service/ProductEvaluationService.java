package org.cn.personalapi.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.PersonalColor;
import org.cn.personalapi.domain.product.dto.response.ProductEvaluationResponse;
import org.cn.personalapi.infrastructure.port.MultimodalVisionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductEvaluationService {

    private final MultimodalVisionPort multimodalVisionPort;
    private final ObjectMapper objectMapper;

    public ProductEvaluationResponse evaluateProduct(MultipartFile productImage, PersonalColor personalColor) {

        String gptResponse = multimodalVisionPort.evaluateProductSuitability(productImage, personalColor);
        log.info("GPT Vision Response: {}", gptResponse);

        try {
            String cleanedResponse = cleanJsonResponse(gptResponse);
            return objectMapper.readValue(cleanedResponse, ProductEvaluationResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse GPT Vision response", e);
            throw new RuntimeException("상품 평가 결과를 파싱하는데 실패했습니다.", e);
        }
    }

    private String cleanJsonResponse(String response) {
        String cleaned = response.strip();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        return cleaned.strip();
    }
}
