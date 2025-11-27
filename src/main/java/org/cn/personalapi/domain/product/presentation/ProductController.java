// src/main/java/org/cn/personalapi/domain/product/presentation/ProductController.java

package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.service.ProductService;
import org.cn.personalapi.domain.review.presentation.ReviewConvertor;
import org.cn.personalapi.global.res.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "fitting - 추천상품 리스트 조회", description = "퍼스널컬러별 추천 상품 목록을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> getProducts(
            @Parameter(description = "로그인 사용자의 ID", example = "1")
            @RequestParam(value = "member-id") Long memberId,
            @Parameter(description = "검색 키워드")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "페이지 (0부터 시작)")
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "페이지 당 사이즈")
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) { // Pageable 추가

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message("상품 리스트 조회 성공")
                        .result(ProductConvertor.toPageListRes(productService.getProducts(memberId, keyword, page, size)))
                        .build());
    }

    @Operation(summary = "fitting - 추천상품 상세 조회", description = "상품의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<?>> getProduct(
            @Parameter(description = "조회할 상품의 ID", example = "1")
            @PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message("상품 상세보기 조회 성공")
                        .result(productService.getProduct(id))
                        .build());
    }

    @Operation(summary = "fitting - 리뷰 조회", description = "상품의 리뷰 목록을 조회합니다.")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<ResponseDto<?>> getReviewByProduct(
            @Parameter(description = "리뷰 목록을 조회할 상품의 ID", example = "1")
            @PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message("상품 리뷰 조회 성공")
                        .result(ReviewConvertor.toListRes(productService.getReviewByProduct(id)))
                        .build());
    }

}