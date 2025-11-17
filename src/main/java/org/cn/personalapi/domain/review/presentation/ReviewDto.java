package org.cn.personalapi.domain.review.presentation;

import lombok.Builder;

import java.time.LocalDateTime;

public class ReviewDto {

    @Builder
    public record ListRes (
        Long id,
        Integer rating,
        Integer likes,
        String content,
        String userName,
        String userDescription,
        String userImage,
        LocalDateTime createdAt
    ) {}
}
