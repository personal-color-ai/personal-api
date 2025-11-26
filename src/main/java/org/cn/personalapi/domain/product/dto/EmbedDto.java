package org.cn.personalapi.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.review.domain.Review;

import java.util.List;

public class EmbedDto {
    @Builder
    public record ProductDTO(
            Long id,
            String name,
            String brand,
            Double rating,
            Integer reviewCountAll,
            String category,
            List<ReviewDTO> review
    ) {
        public static ProductDTO toDto(Product product) {
            return ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand())
                    .rating(product.getRating())
                    .reviewCountAll(product.getReviewCount())
                    .category(product.getCategory().name()) // [추가] "BASE", "LIP" 등으로 변환
                    .review(product.getReviews().stream().map(ReviewDTO::toDto).toList())
                    .build();
        }
    }

    @Builder
    public record ReviewDTO(
            Integer rating,
            Integer likes,
            String content,
            String userDescription
    ) {
        public static ReviewDTO toDto(Review review) {
            return ReviewDTO.builder()
                    .rating(review.getRating())
                    .likes(review.getLikes())
                    .content(review.getContent())
                    .userDescription(review.getUserDescription())
                    .build();
        }
    }


    public record FastEmbeddingResponse(
        String message,
        @JsonProperty("processed_products_count")
        int processedProductsCount,
        @JsonProperty("total_embedded_reviews")
        int totalEmbeddedReviews
    ) {}

    // 검색 요청 DTO
    public record SearchReq(
            @JsonProperty("personal_color")
            String personalColor,
            String prompt
    ) {}

    // 검색 응답 DTO
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchRes(
            List<SearchResultItem> results
    ) {}

    // 검색 결과 아이템 DTO
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchResultItem(
            @JsonProperty("product_id")
            Long productId,

            @JsonProperty("similarity_distance")
            Double similarityDistance
    ) {}
}
