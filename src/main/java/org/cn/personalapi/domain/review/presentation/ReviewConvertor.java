package org.cn.personalapi.domain.review.presentation;

import org.cn.personalapi.domain.review.domain.Review;

import java.util.List;

public class ReviewConvertor {

    public static List<ReviewDto.ListRes> toListRes(List<Review> reviews) {
        return reviews.stream()
                .map(review -> ReviewDto.ListRes.builder()
                        .id(review.getId())
                        .rating(review.getRating())
                        .likes(review.getLikes())
                        .content(review.getContent())
                        .userName(review.getUserName())
                        .userDescription(review.getUserDescription())
                        .userImage(review.getUserImage())
                        .createdAt(review.getCreatedAt())
                        .build())
                .toList();
    }
}
