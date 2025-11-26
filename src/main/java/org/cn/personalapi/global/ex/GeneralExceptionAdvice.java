package org.cn.personalapi.global.ex;

import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.global.res.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> exception(CustomException e, WebRequest request) {
        log.error("정의된 오류 발생 : {}", e.getMessage(), e);

        ResponseDto<Object> body = ResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .result(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        log.error("Spring app 오류가 발생했습니다. : {}", e.getMessage(), e);

        ResponseDto<Object> body = ResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Spring app : 서버 오류가 발생했습니다.")
                .result(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
