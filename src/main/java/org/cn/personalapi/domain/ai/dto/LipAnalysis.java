package org.cn.personalapi.domain.ai.dto;

import java.util.Map;

public record LipAnalysis(
    String result,
    Map<String, Double> probs
) {
}
