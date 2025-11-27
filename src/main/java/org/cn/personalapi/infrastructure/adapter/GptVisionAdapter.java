package org.cn.personalapi.infrastructure.adapter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.PersonalColor;
import org.cn.personalapi.infrastructure.adapter.dto.ChatGptRequest;
import org.cn.personalapi.infrastructure.adapter.dto.ChatGptResponse;
import org.cn.personalapi.infrastructure.adapter.dto.ImageContent;
import org.cn.personalapi.infrastructure.adapter.dto.ImageUrl;
import org.cn.personalapi.infrastructure.adapter.dto.Message;
import org.cn.personalapi.infrastructure.adapter.dto.TextContent;
import org.cn.personalapi.infrastructure.port.MultimodalVisionPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class GptVisionAdapter implements MultimodalVisionPort {

    private final WebClient webClient;

    public GptVisionAdapter(@Qualifier("externalWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String analyzeImageWithText(MultipartFile imageFile, String textPrompt) {
        try {
            String base64Image = encodeImageToBase64(imageFile);
            String imageUrl = "data:image/jpeg;base64," + base64Image;

            Message message = Message.builder()
                .role("user")
                .content(List.of(
                    new TextContent(textPrompt),
                    new ImageContent(new ImageUrl(imageUrl))
                ))
                .build();

            ChatGptRequest request = ChatGptRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(message))
                .temperature(0.7)
                .max_tokens(1000)
                .build();

            return callGptApi(request);

        } catch (IOException e) {
            log.error("Failed to encode image to Base64", e);
            throw new RuntimeException("이미지 인코딩에 실패했습니다.", e);
        }
    }

    @Override
    public String evaluateProductSuitability(MultipartFile productImage, PersonalColor personalColor) {
        String prompt = createEvaluationPrompt(personalColor);

        try {
            String base64Image = encodeImageToBase64(productImage);
            String imageUrl = "data:image/jpeg;base64," + base64Image;

            Message message = Message.builder()
                .role("user")
                .content(List.of(
                    new TextContent(prompt),
                    new ImageContent(new ImageUrl(imageUrl))
                ))
                .build();

            ChatGptRequest request = ChatGptRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(message))
                .temperature(0.3)
                .max_tokens(800)
                .responseFormat(Map.of("type", "json_object"))
                .build();

            return callGptApi(request);

        } catch (IOException e) {
            log.error("Failed to evaluate product suitability", e);
            throw new RuntimeException("상품 평가에 실패했습니다.", e);
        }
    }

    private String createEvaluationPrompt(PersonalColor personalColor) {
        return String.format("""
                당신은 퍼스널 컬러 전문가입니다. 제공된 상품 이미지를 분석하여 '%s(%s)' 타입의 사용자에게 얼마나 어울리는지 평가해주세요.
                
                평가 기준:
                1. 색상 톤 (따뜻함/차가움)
                2. 명도 (밝기)
                3. 채도 (선명도)
                4. 전체적인 조화
                
                다음 JSON 형식으로 응답해주세요:
                {
                  "suitable": true/false,
                  "score": 0-100,
                  "reason": "평가 이유 (2-3문장)",
                  "colorAnalysis": "상품의 색상 분석",
                  "recommendation": "스타일링 추천사항"
                }
                """,
            personalColor.getKoreanName(),
            personalColor.getDescription()
        );
    }

    private String encodeImageToBase64(MultipartFile imageFile) throws IOException {
        byte[] imageBytes = imageFile.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private String callGptApi(ChatGptRequest request) {
        long startTime = System.nanoTime();

        ChatGptResponse response = webClient.post()
            .uri("/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(ChatGptResponse.class)
            .block();

        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;
        log.info("GPT Vision API 호출 시간: {}ms", durationMillis);

        if (response == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("GPT Vision API 응답을 가져오지 못했습니다.");
        }

        return response.getChoices().getFirst().getMessage().getContent().toString();
    }
}
