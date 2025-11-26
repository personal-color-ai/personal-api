package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.domain.Category;
import org.cn.personalapi.domain.product.dto.CrawlingDto;
import org.cn.personalapi.domain.product.dto.ProductDto;
import org.cn.personalapi.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/crawling")
public class CrawlingController {

    private final ProductService productService;

    @Operation(summary = "[연결x]패션 크롤링 테스트용 엔드포인트", description = "패션 크롤링 엔드포인트입니다.")
    @PostMapping("/crawling-fashion")
    public ResponseEntity<String> crawlingFashion() {
        return ResponseEntity.ok()
                .body("미완");
    }

    @Operation(summary = "뷰티 크롤링 테스트용 엔드포인트", description = "뷰티 크롤링 엔드포인트입니다.")
    @PostMapping("/crawling-beauty")
    public ResponseEntity<String> crawlingBeauty(CrawlingDto.BeautyReq dto) {
        productService.crawlingBeauty(dto);
        return ResponseEntity.ok()
                .body("뷰티 크롤링 성공");
    }

    @Operation(summary = "뷰티-리뷰 크롤링 테스트용 엔드포인트", description = "뷰티-리뷰 크롤링 엔드포인트입니다.")
    @PostMapping("/crawling-beauty-review")
    public ResponseEntity<String> crawlingReview(
            ProductDto.ReviewReq request
    ) {
        productService.crawlingReview();
        return ResponseEntity.ok()
                .body("뷰티-리뷰 크롤링 성공");
    }

    @Operation(summary = "뷰티-옵션 크롤링 테스트용 엔드포인트", description = "뷰티-옵션 크롤링 엔드포인트입니다.")
    @PostMapping("/crawling-beauty-option")
    public ResponseEntity<String> crawlingOptions(
            ProductDto.ReviewReq request
    ) {
        productService.crawlingOptions();
        return ResponseEntity.ok()
                .body("뷰티-옵션 크롤링 성공");
    }


}
