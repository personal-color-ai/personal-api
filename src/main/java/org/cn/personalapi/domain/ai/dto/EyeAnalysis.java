package org.cn.personalapi.domain.ai.dto;

import java.util.Map;

public record EyeAnalysis(
    String result,
    Map<String, Double> probs
) {
}
