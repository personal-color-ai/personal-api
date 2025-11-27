package org.cn.personalapi.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.PersonalColor;
import org.cn.personalapi.domain.product.dto.response.ProductEvaluationResponse;
import org.cn.personalapi.domain.product.service.ProductEvaluationService;
import org.cn.personalapi.global.res.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductEvaluationController {

    private final ProductEvaluationService productEvaluationService;

    @PostMapping(value = "/evaluate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<ProductEvaluationResponse> evaluateProduct(
        @RequestPart(value = "productImage") MultipartFile productImage,
        @RequestParam(value = "personalColor") PersonalColor personalColor
    ) {
        ProductEvaluationResponse response = productEvaluationService.evaluateProduct(
            productImage,
            personalColor
        );
        return ResponseDto.success(response);
    }
}
