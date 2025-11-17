package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.service.ProductService;
import org.cn.personalapi.domain.review.presentation.ReviewConvertor;
import org.cn.personalapi.domain.review.presentation.ReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "fitting - 추천상품 리스트 조회", description = "퍼스널컬러별 추천 상품 목록을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<List<ProductDto.ListRes>> getProducts(
            @Parameter(description = "로그인 사용자의 ID", example = "1")
            @RequestParam(value = "member-id")  Long memberId) {
        return ResponseEntity.ok()
                .body(ProductConvertor.toListResList(productService.getProducts(memberId)));
    }

    @Operation(summary = "fitting - 추천상품 상세 조회", description = "상품의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.DetailRes> getProduct(
            @Parameter(description = "조회할 상품의 ID", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(productService.getProduct(id));
    }

    @Operation(summary = "fitting - 리뷰 조회", description = "상품의 리뷰 목록을 조회합니다.")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDto.ListRes>> getReviewByProduct(
            @Parameter(description = "리뷰 목록을 조회할 상품의 ID", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(ReviewConvertor.toListRes(productService.getReviewByProduct(id)));
    }

}
