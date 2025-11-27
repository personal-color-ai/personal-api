package org.cn.personalapi.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;
import org.cn.personalapi.domain.ai.service.AIService;
import org.cn.personalapi.domain.report.dto.response.ComplexReportResponseDto;
import org.cn.personalapi.domain.report.dto.response.PersonalColorReportDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AIService aiService;

    public ComplexReportResponseDto getReport(MultipartFile imageFile) {
        PersonalColorResponse personalColorResponse = aiService.extractPersonalColor(imageFile);
        PersonalColorReportDto personalColorReportDto = aiService.getPersonalColorReportDto(personalColorResponse);
        return new ComplexReportResponseDto(personalColorResponse, personalColorReportDto);
    }
}
