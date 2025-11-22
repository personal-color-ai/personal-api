package org.cn.personalapi.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.ai.dto.PersonalColorResponse;
import org.cn.personalapi.domain.ai.service.AIService;
import org.cn.personalapi.domain.report.dto.response.PersonalColorReportDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AIService aiService;

    public PersonalColorReportDto getReport(MultipartFile imageFile) {
        PersonalColorResponse personalColorResponse = aiService.extractPersonalColor(imageFile);
        return aiService.getPersonalColorReportDto(personalColorResponse);
    }
}
