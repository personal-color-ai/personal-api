package org.cn.personalapi.infrastructure.port;

import org.cn.personalapi.domain.PersonalColor;
import org.springframework.web.multipart.MultipartFile;

public interface MultimodalVisionPort {

    /**
     * 이미지와 텍스트를 함께 분석하는 멀티모달 요청
     *
     * @param imageFile 분석할 이미지 파일
     * @param textPrompt 분석 요청 텍스트
     * @return AI 응답 텍스트
     */
    String analyzeImageWithText(MultipartFile imageFile, String textPrompt);

    /**
     * 상품 이미지와 퍼스널 컬러를 기반으로 적합성 평가
     *
     * @param productImage 상품 이미지
     * @param personalColor 사용자의 퍼스널 컬러
     * @return JSON 형식의 평가 결과
     */
    String evaluateProductSuitability(MultipartFile productImage, PersonalColor personalColor);
}
