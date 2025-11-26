package org.cn.personalapi.domain.ai.dto;

public record PersonalColorResponse(
    String message,
    ImageAnalysis image,
    LipAnalysis lip,
    EyeAnalysis eye
) {
}
