package org.cn.personalapi.domain.ai.prompt;

import java.util.Objects;
import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;


public class Prompt {

    private final String content;

    /**
     * PersonalColorResponse 객체를 기반으로 Prompt 객체를 생성합니다.
     *
     * @param personalColorResponse AI 모델로부터 받은 퍼스널 컬러 분석 결과
     * @return 완성된 Prompt 객체
     */
    public static Prompt create(PersonalColorResponse personalColorResponse) {
        // null 체크 및 유효성 검사
        if (Objects.isNull(personalColorResponse)) {
            throw new IllegalArgumentException("PersonalColorResponse cannot be null.");
        }

        // 퍼스널 컬러 정보 추출
        String colorType = personalColorResponse.image().result();
        String lipColor = personalColorResponse.lip().result();

        String promptContent = buildPromptContent(colorType, lipColor);

        return new Prompt(promptContent);
    }

    private Prompt(String content) {
        this.content = content;
    }

    /**
     * LLM에게 전달할 최종 프롬프트 내용을 반환합니다.
     *
     * @return 프롬프트 문자열
     */
    public String getContent() {
        return content;
    }

    /**
     * LLM에게 보낼 구체적인 질문 내용을 조합하는 메소드
     */
    private static String buildPromptContent(String colorType, String lipColor) {
        return """
            당신은 전문 퍼스널 컬러 스타일리스트입니다. 아래의 분석 결과를 바탕으로 상세한 스타일링 가이드를 JSON 형식으로 제공해 주세요.

            --- 분석 결과 ---
            퍼스널 컬러: %s
            립 컬러: %s

            --- 응답 형식 ---
            반드시 아래의 JSON 형식으로만 응답해 주세요. 다른 설명이나 텍스트 없이 순수한 JSON만 반환하세요.

            {
              "summary": {
                "content": "해당 퍼스널 컬러 타입에 대한 전반적인 특징과 이미지를 1-2문장으로 설명"
              },
              "color": {
                "colorType": "퍼스널 컬러 타입 이름 (예: 가을 웜 뮤트)",
                "bestColors": ["어울리는 색상 3-5개"],
                "worstColors": ["피해야 할 색상 3개"]
              },
              "fashion": {
                "recommendedMaterials": ["추천 소재 3-4개"],
                "recommendedPatterns": ["추천 패턴 2-3개"]
              },
              "beauty": {
                "lipColors": ["추천 립 컬러 3개"],
                "eyeShadows": ["추천 아이섀도우 3개"],
                "hairColors": ["추천 헤어 컬러 3개"],
                "jewelry": ["추천 악세서리 색상 3개"]
              }
            }

            주의사항:
            - 반드시 위의 JSON 구조를 정확히 따라야 합니다.
            - 한국어로 작성해 주세요.
            - 구체적이고 실용적인 추천을 제공해 주세요.
            - JSON 외의 다른 텍스트는 포함하지 마세요.
            """.formatted(colorType, lipColor);
    }
}
