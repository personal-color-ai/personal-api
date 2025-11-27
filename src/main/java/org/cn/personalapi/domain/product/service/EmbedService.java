package org.cn.personalapi.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.product.domain.Category;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.product.dto.EmbedDto;
import org.cn.personalapi.domain.product.repository.ProductRepository;
import org.cn.personalapi.domain.user.domain.User;
import org.cn.personalapi.domain.user.repository.UserRepository;
import org.cn.personalapi.global.ex.CustomException;
import org.cn.personalapi.infra.FastAppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class EmbedService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FastAppUtil fastAppUtil;

    public void embedBeauty(Category category) {
        List<Product> products = productRepository.findByCategory(category);

        List<EmbedDto.ProductDTO> dto = products.stream().map(EmbedDto.ProductDTO::toDto).toList();

        EmbedDto.FastEmbeddingResponse reuslt = fastAppUtil.embedBeauty(dto);

        log.info("임베딩 결과 : {}", reuslt);
    }


    // 프롬프트 기반 상품 추천 조회
    public Page<Product> getProducts(Long memberId, String prompt, Integer page, Integer size) {
        // 1. 사용자 정보 조회 (퍼스널 컬러 확인용)
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        String personalColor = user.getPersonalType().getValue();

        // 2. 파이썬 API 호출하여 추천 상품 ID 리스트 획득
        List<Long> recommendedProductIds = fastAppUtil.searchProducts(personalColor, prompt);
        log.info("프롬프트 검색 결과{}", recommendedProductIds);

        if (recommendedProductIds.isEmpty()) {
            log.warn("프롬프트 검색 결과 없음");
            return Page.empty(PageRequest.of(page, size));
        }

        // 3. DB에서 상품 정보 조회 (findAllById는 순서를 보장하지 않음)
        List<Product> products = productRepository.findAllById(recommendedProductIds);

        // 4. 추천 순서(recommendedProductIds의 순서)대로 정렬
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Product> sortedProducts = recommendedProductIds.stream()
                .filter(productMap::containsKey) // DB에 존재하는 것만 필터링
                .map(productMap::get)
                .toList();

        return new PageImpl<>(sortedProducts, PageRequest.of(page, size), sortedProducts.size());
    }
}
