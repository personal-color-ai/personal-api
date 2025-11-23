package org.cn.personalapi.domain.report.controller;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.report.dto.response.PersonalColorReportDto;
import org.cn.personalapi.domain.report.service.ReportService;
import org.cn.personalapi.global.ResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseDto<?> getReport(
        @RequestPart(value = "file") MultipartFile imageFile
    ) {
        PersonalColorReportDto report = reportService.getReport(imageFile);
        return ResponseDto.success(report);
    }
}
