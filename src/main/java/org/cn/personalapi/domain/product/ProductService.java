package org.cn.personalapi.domain.product;

import lombok.RequiredArgsConstructor;
import org.cn.personalapi.domain.review.Review;
import org.cn.personalapi.domain.review.ReviewRepository;
import org.cn.personalapi.infra.FastAppUtil;
import org.cn.personalapi.infra.FastDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OptionRepository optionRepository;
    private final FastAppUtil fastAppUtil;

    public List<Product> getProducts(Long memberId) {
        // TODO 추천 로직으로 변경 필요
        // python 에서 추천 로직? 이거 지피티 찾아보기
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
        List<FastDto.Fashion> fashions = fastAppUtil.crawlingFashion();
    }

    @Transactional
    public void crawlingBeauty() {
        List<FastDto.Beauty> beautyList = fastAppUtil.crawlingBeauty();

        List<Product> products = beautyList.stream().map(beauty -> {
            if (productRepository.findByGoodsId(beauty.productId()).isPresent()) {
                return null;
            }

            return Product.builder()
                    .goodsId(beauty.productId())
                    .name(beauty.name())
                    .brand(beauty.brand())
                    .price(beauty.finalPrice())
                    .rating(beauty.reviewScore() != null ? beauty.reviewScore().doubleValue() : 0.0)
                    .reviewCount(beauty.reviewCount() != null ? beauty.reviewCount() : 0)
                    .url(beauty.productUrl())
                    .imageUrl(beauty.imageUrl())
                    .build();
        }).toList();

        if (products.isEmpty()) return;

        productRepository.saveAll(products);
    }

    @Transactional
    public void crawlingReview() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (!product.getReviews().isEmpty()) continue;

            List<FastDto.Review> reviews = fastAppUtil.crawlingReview(product.getGoodsId());

            List<Review> reviewEntities = reviews.stream().map(review -> {
                return Review.builder()
                        .product(product)
                        .userName(review.userName())
                        .userDescription(review.userProfile())
                        .userImage(review.userImage())
                        .rating(Integer.valueOf(review.grade()))
                        .likes(review.likeCount())
                        .content(review.content())
                        .build();
            }).toList();

            reviewRepository.saveAll(reviewEntities);
        }
    }

    @Transactional
    public void crawlingOptions() {
        List<Product> products = productRepository.findAll();
        List<Option> optionEntities = new ArrayList<>();

        for (Product product : products) {
            if (!product.getOptions().isEmpty()) continue;

            List<FastDto.Option> options = fastAppUtil.crawlingOption(product.getGoodsId());

            List<Option> entity = options.stream().map(option -> {
                return Option.builder()
                        .product(product)
                        .name(option.name())
                        .imageUrl(option.imageUrl())
                        .optionNo(option.optionNo())
                        .build();
            }).toList();
            optionEntities.addAll(entity);
        }

        if (optionEntities.isEmpty()) return;

        optionRepository.saveAll(optionEntities);
    }
}
