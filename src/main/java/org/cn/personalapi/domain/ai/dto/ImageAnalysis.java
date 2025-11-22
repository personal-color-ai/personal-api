package org.cn.personalapi.domain.ai.dto;

import java.util.Map;

public record ImageAnalysis(
    String result,
    Map<String, Double> probs) {
}
