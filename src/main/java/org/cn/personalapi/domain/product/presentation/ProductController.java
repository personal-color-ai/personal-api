// src/main/java/org/cn/personalapi/domain/product/presentation/ProductController.java

package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.service.ProductService;
import org.cn.personalapi.domain.review.presentation.ReviewConvertor;
import org.cn.personalapi.domain.review.presentation.ReviewDto;
import org.cn.personalapi.global.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<PageDto.Dto> getProducts(
            @Parameter(description = "로그인 사용자의 ID", example = "1")
            @RequestParam(value = "member-id") Long memberId,
            @Parameter(description = "페이징 정보")
            @PageableDefault(size = 20) Pageable pageable) { // Pageable 추가

        return ResponseEntity.ok()
                .body(ProductConvertor.toPageListRes(productService.getProducts(memberId, pageable)));
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