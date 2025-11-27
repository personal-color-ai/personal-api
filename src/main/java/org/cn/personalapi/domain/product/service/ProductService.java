package org.cn.personalapi.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cn.personalapi.domain.product.domain.Option;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.product.dto.CrawlingDto;
import org.cn.personalapi.domain.product.dto.ProductDto;
import org.cn.personalapi.domain.product.presentation.ProductConvertor;
import org.cn.personalapi.domain.product.repository.OptionRepository;
import org.cn.personalapi.domain.product.repository.ProductRepository;
import org.cn.personalapi.domain.review.domain.Review;
import org.cn.personalapi.domain.review.domain.ReviewRepository;
import org.cn.personalapi.domain.user.domain.PersonalType;
import org.cn.personalapi.domain.user.domain.User;
import org.cn.personalapi.domain.user.repository.UserRepository;
import org.cn.personalapi.global.ex.CustomException;
import org.cn.personalapi.infra.FastAppUtil;
import org.cn.personalapi.infra.FastDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
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
    private final UserRepository userRepository;
    private final FastAppUtil fastAppUtil;

    private static final int MINIMUM_REVIEWS = 5;

    @Transactional(readOnly = true)
    public Page<Product> getProducts(Long memberId, String keyword, Integer page, Integer size) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        // 1. 퍼스널 컬러 키워드 (리뷰 매칭용)
        String personalColorKeyword = user.getPersonalType().getValue(); // 또는 .name() 확인 필요 (User.java의 PersonalType enum 확인)

        // 2. 전체 평균 계산 (최적화: count 쿼리 사용)
        long totalReviews = reviewRepository.count();
        long matchingReviews = reviewRepository.countByUserDescriptionContaining(personalColorKeyword);
        double overallAverage = (totalReviews > 0) ? (double) matchingReviews / totalReviews : 0.0;

        // 3. 쿼리 호출 (검색어 + 정렬)
        Pageable pageable = PageRequest.of(page, size);

        // keyword가 null이거나 빈 문자열이면 null로 처리하여 쿼리로 전달
        String searchParam = (keyword != null && !keyword.isBlank()) ? keyword : null;

        return productRepository.findProductsSortedByBayesianScore(
                personalColorKeyword,  // 리뷰 점수 계산용 (내 퍼스널 컬러)
                searchParam,           // 상품 검색용 (사용자 입력 키워드)
                overallAverage,
                MINIMUM_REVIEWS,
                pageable
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
}
