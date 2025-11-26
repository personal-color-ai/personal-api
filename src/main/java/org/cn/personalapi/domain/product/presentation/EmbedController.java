package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.domain.Category;
import org.cn.personalapi.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/em")
public class EmbedController {

    private final ProductService productService;

    @Operation(summary = "제품 카테고리별 임베딩", description = "제품 카테고리별 임베딩")
    @PostMapping("/beauty/{category}")
    public ResponseEntity<String> emBeauty(@PathVariable("category") Category category) {
        productService.embedBeauty(category);
        return ResponseEntity.ok()
                .body("뷰티-상품 임베딩 성공");
    }
}
