package org.cn.personalapi.global;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.cn.personalapi.domain.report.dto.response.PersonalColorReportDto;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"status", "message", "result"})
@Schema(description = "공통 응답")
@Data
@Builder
public class ResponseDto<T> {
    @Schema(example = "BAD_REQUEST 또는 INTERNAL_SERVER_ERROR")
    private HttpStatus status;
    @Schema(example = "오류 메시지")
    private String message;
    private T result;

    public static ResponseDto<Object> success(PersonalColorReportDto report) {
        return ResponseDto.builder()
            .status(HttpStatus.OK)
            .result(report)
            .build();
    }
}
