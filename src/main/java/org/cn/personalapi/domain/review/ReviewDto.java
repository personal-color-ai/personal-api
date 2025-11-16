package org.cn.personalapi.domain.review;

import lombok.Builder;
import org.cn.personalapi.domain.user.PersonalType;
import org.cn.personalapi.domain.user.UserDto;

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
