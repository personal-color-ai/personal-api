package org.cn.personalapi.domain.report.controller;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.report.dto.response.ComplexReportResponseDto;
import org.cn.personalapi.domain.report.service.ReportService;
import org.cn.personalapi.global.res.ResponseDto;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<?> getReport(
        @RequestPart(value = "file") MultipartFile imageFile
    ) {
        ComplexReportResponseDto report = reportService.getReport(imageFile);
        return ResponseDto.success(report);
    }
}
