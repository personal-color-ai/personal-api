package org.cn.personalapi.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.product.domain.Category;
import org.cn.personalapi.domain.product.domain.Option;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.product.dto.CrawlingDto;
import org.cn.personalapi.domain.product.dto.EmbedDto;
import org.cn.personalapi.domain.product.dto.ProductDto;
import org.cn.personalapi.domain.product.presentation.ProductConvertor;
import org.cn.personalapi.domain.product.repository.OptionRepository;
import org.cn.personalapi.domain.product.repository.ProductRepository;
import org.cn.personalapi.domain.review.domain.Review;
import org.cn.personalapi.domain.review.domain.ReviewRepository;
import org.cn.personalapi.domain.user.domain.User;
import org.cn.personalapi.domain.user.repository.UserRepository;
import org.cn.personalapi.global.CustomException;
import org.cn.personalapi.infra.FastAppUtil;
import org.cn.personalapi.infra.FastDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository; // UserRepository 주입
    private final FastAppUtil fastAppUtil;

    // 전체 조회 -> 페이징 조회로 변경 [Critical B 해결]
    public Page<Product> getProducts(Long memberId, Integer page, Integer size) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));
        String searchKeyword = "%" + user.getPersonalType().getValue() + "%"; // keyword 통일

        // 1. 전체 평균(C) 계산
        // 전체 리뷰 수
        long totalReviews = reviewRepository.count();
        // 매칭되는 전체 리뷰 수 (count 쿼리 활용)
        long totalMatchingReviews = reviewRepository.countByUserDescriptionContaining(searchKeyword);

        double overallAverage = (totalReviews > 0) ? (double) totalMatchingReviews / totalReviews : 0.0;
        final int MINIMUM_REVIEWS = 5;

        // 2. DB 레벨에서 베이지안 점수 계산 및 페이징
        return productRepository.findProductsSortedByBayesianScore(
                searchKeyword,
                overallAverage,
                MINIMUM_REVIEWS,
                PageRequest.of(page, size)
        );
    }

    @Transactional(readOnly = true)
    public ProductDto.DetailRes getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품이 없습니다. product ID: " + id));

        return ProductConvertor.toDetailRes(product);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Transactional
    public void crawlingFashion() {
        // TODO 패션 크롤링 구현 필요
    }

    @Transactional
    public void crawlingBeauty(CrawlingDto.BeautyReq dto) {
        List<FastDto.Beauty> beautyList = fastAppUtil.crawlingBeauty(dto);

        // N+1 문제 해결: goodsId 목록 추출 후 일괄 조회
        List<String> goodsIds = beautyList.stream()
                .map(FastDto.Beauty::productId)
                .toList();

        Set<String> existingGoodsIds = productRepository.findAllByGoodsIdIn(goodsIds).stream()
                .map(Product::getGoodsId)
                .collect(Collectors.toSet());

        List<Product> newProducts = beautyList.stream()
                .filter(beauty -> !existingGoodsIds.contains(beauty.productId())) // 메모리 상에서 중복 제거
                .map(beauty -> Product.builder()
                        .goodsId(beauty.productId())
                        .name(beauty.name())
                        .brand(beauty.brand())
                        .price(beauty.finalPrice())
                        .rating(beauty.reviewScore() != null ? beauty.reviewScore().doubleValue() : 0.0)
                        .reviewCount(beauty.reviewCount() != null ? beauty.reviewCount() : 0)
                        .url(beauty.productUrl())
                        .imageUrl(beauty.imageUrl())
                        .category(dto.category())
                        .build())
                .toList();

        if (!newProducts.isEmpty()) {
            productRepository.saveAll(newProducts);
        }
    }

    public void crawlingReview() {
        // 리뷰가 없는 상품만 DB에서 조회 (메모리 필터링 제거)
        List<Product> products = productRepository.findByReviewsIsEmpty();

        products.forEach(product -> {
            List<FastDto.Review> reviews = fastAppUtil.crawlingReview(product.getGoodsId());
            List<Review> reviewEntities = reviews.stream()
                    .map(review -> Review.builder()
                            .product(product)
                            .userName(review.userName())
                            .userDescription(review.userProfile())
                            .userImage(review.userImage())
                            .rating(parseGrade(review.grade()))
                            .likes(review.likeCount())
                            .content(review.content())
                            .build())
                    .toList();

            // saveAll은 자체적으로 트랜잭션 처리됨
            if (!reviewEntities.isEmpty()) {
                reviewRepository.saveAll(reviewEntities);
            }
        });
    }

    public void crawlingOptions() {
        // 옵션이 없는 상품만 DB에서 조회
        List<Product> products = productRepository.findByOptionsIsEmpty();

        products.forEach(product -> {
            List<Option> options = fastAppUtil.crawlingOption(product.getGoodsId()).stream()
                    .map(option -> Option.builder()
                            .product(product)
                            .name(option.name())
                            .imageUrl(option.imageUrl())
                            .optionNo(option.optionNo())
                            .build())
                    .toList();

            if (!options.isEmpty()) {
                optionRepository.saveAll(options);
            }
        });
    }

    private Integer parseGrade(String grade) {
        try {
            return Integer.valueOf(grade);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void embedBeauty(Category category) {
        List<Product> products = productRepository.findByCategory(category);

        List<EmbedDto.ProductDTO> dto = products.stream().map(EmbedDto.ProductDTO::toDto).toList();

        EmbedDto.FastEmbeddingResponse reuslt = fastAppUtil.embedBeauty(dto);

        log.info("임베딩 결과 : {}", reuslt);
    }
}
