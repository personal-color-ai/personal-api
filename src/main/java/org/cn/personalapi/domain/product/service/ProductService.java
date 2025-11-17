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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OptionRepository optionRepository;
    private final FastAppUtil fastAppUtil;

    public List<Product> getProducts(Long memberId) {
        // TODO 추천 로직으로 변경 필요
        return productRepository.findAll();
    }

    public ProductDto.DetailRes getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다. product ID: " + id));

        return ProductConvertor.toDetailRes(product);
    }

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

        List<Product> products = beautyList.stream()
                .filter(beauty -> productRepository.findByGoodsId(beauty.productId()).isEmpty()) // 중복 제거
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

        saveEntities(products, productRepository::saveAll);
    }

    @Transactional
    public void crawlingReview() {
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> product.getReviews().isEmpty()) // 리뷰가 없는 상품만 필터링
                .toList();

        products.forEach(product -> {
            List<FastDto.Review> reviews = fastAppUtil.crawlingReview(product.getGoodsId());
            List<Review> reviewEntities = reviews.stream()
                    .map(review -> Review.builder()
                            .product(product)
                            .userName(review.userName())
                            .userDescription(review.userProfile())
                            .userImage(review.userImage())
                            .rating(Integer.valueOf(review.grade()))
                            .likes(review.likeCount())
                            .content(review.content())
                            .build())
                    .toList();

            saveEntities(reviewEntities, reviewRepository::saveAll);
        });
    }

    @Transactional
    public void crawlingOptions() {
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> product.getOptions().isEmpty()) // 옵션이 없는 상품만 필터링
                .toList();

        products.forEach(product -> {
            List<Option> options = fastAppUtil.crawlingOption(product.getGoodsId()).stream()
                    .map(option -> Option.builder()
                            .product(product)
                            .name(option.name())
                            .imageUrl(option.imageUrl())
                            .optionNo(option.optionNo())
                            .build())
                    .toList();

            optionRepository.saveAll(options); // 바로 저장
        });
    }


    // 공통 저장 메서드
    private <T> void saveEntities(List<T> entities, Consumer<List<T>> saveFunction) {
        if (!entities.isEmpty()) {
            saveFunction.accept(entities);
        }
    }
}
