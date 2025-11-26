package org.cn.personalapi.domain.product.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.domain.Category;
import org.cn.personalapi.domain.product.service.EmbedService;
import org.cn.personalapi.domain.product.service.ProductService;
import org.cn.personalapi.global.res.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/em")
public class EmbedController {

    private final EmbedService embedService;

    @Operation(summary = "제품 카테고리별 임베딩", description = "제품 카테고리별 임베딩")
    @PostMapping("/beauty/{category}")
    public ResponseEntity<String> emBeauty(@PathVariable("category") Category category) {
        embedService.embedBeauty(category);
        return ResponseEntity.ok()
                .body("뷰티-상품 임베딩 성공");
    }

    @Operation(summary = "fitting - 프롬프트 리스트 조회", description = "요청사항별 추천 상품 목록을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<ResponseDto<?>> getProducts(
            @Parameter(description = "로그인 사용자의 ID", example = "1")
            @RequestParam(value = "member-id") Long memberId,

            @Parameter(description = "검색 프롬프트", example = "지속력 좋은 틴트 추천해줘")
            @RequestParam(value = "prompt") String prompt,

            @Parameter(description = "페이지 (0부터 시작)")
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,

            @Parameter(description = "페이지 당 사이즈")
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message("상품 리스트 조회 성공")
                        .result(ProductConvertor.toPageListRes(embedService.getProducts(memberId, prompt, page, size)))
                        .build());
    }
}
