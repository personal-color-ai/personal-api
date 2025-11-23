package org.cn.personalapi.domain.report.dto.response;

import java.util.List;

public record ColorRecommendationDto(
    String colorType,
    List<String> bestColors,
    List<String> worstColors
) {

}
