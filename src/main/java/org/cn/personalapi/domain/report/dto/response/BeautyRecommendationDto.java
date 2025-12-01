package org.cn.personalapi.domain.report.dto.response;

import java.util.List;

public record BeautyRecommendationDto(
    List<ColorDto> lipColors,
    List<ColorDto> eyeShadows,
    List<ColorDto> hairColors,
    List<String> jewelry
) {
}
