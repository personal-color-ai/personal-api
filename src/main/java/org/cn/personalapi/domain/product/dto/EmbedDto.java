package org.cn.personalapi.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
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
            List<ReviewDTO> review
    ) {
        public static ProductDTO toDto(Product product) {
            return ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand())
                    .rating(product.getRating())
                    .reviewCountAll(product.getReviewCount())
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
}
