// src/main/java/org/cn/personalapi/domain/product/service/ProductService.java

package org.cn.personalapi.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.product.presentation.ProductConvertor;
import org.cn.personalapi.domain.product.presentation.ProductDto;
import org.cn.personalapi.domain.product.domain.Option;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.product.repository.OptionRepository;
import org.cn.personalapi.domain.product.repository.ProductRepository;
import org.cn.personalapi.domain.review.domain.Review;
import org.cn.personalapi.domain.review.domain.ReviewRepository;
import org.cn.personalapi.infra.FastAppUtil;
import org.cn.personalapi.infra.FastDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OptionRepository optionRepository;
    private final FastAppUtil fastAppUtil;

    // 전체 조회 -> 페이징 조회로 변경 [Critical B 해결]
    @Transactional(readOnly = true)
    public Page<Product> getProducts(Long memberId, Pageable pageable) {
        // TODO 추천 로직으로 변경 필요
        return productRepository.findAll(pageable);
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
    public void crawlingBeauty() {
        List<FastDto.Beauty> beautyList = fastAppUtil.crawlingBeauty();

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