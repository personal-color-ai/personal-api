package org.cn.personalapi.domain.report.dto.response;

import java.util.List;

public record FashionRecommendationDto(
    List<String> recommendedMaterials,

    List<String> recommendedPatterns
) {
}
