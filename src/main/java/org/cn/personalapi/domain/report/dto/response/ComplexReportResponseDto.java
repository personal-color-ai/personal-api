package org.cn.personalapi.domain.report.dto.response;

import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;

public record ComplexReportResponseDto(
    PersonalColorResponse personalColorResponse,
    PersonalColorReportDto personalColorReportDto
) {
}
