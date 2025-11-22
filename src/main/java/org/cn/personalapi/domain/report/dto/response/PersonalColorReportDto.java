package org.cn.personalapi.domain.report.dto.response;

public record PersonalColorReportDto(
    SummaryDto summary,

    ColorRecommendationDto color,

    FashionRecommendationDto fashion,

    BeautyRecommendationDto beauty
) {

}
