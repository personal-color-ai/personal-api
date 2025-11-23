package org.cn.personalapi.domain.report.dto.response;

import java.util.List;

public record BeautyRecommendationDto(
    List<String> lipColors,
    List<String> eyeShadows,
    List<String> hairColors,
    List<String> jewelry
) {
}
