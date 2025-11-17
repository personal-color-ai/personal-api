package org.cn.personalapi.domain.user.presentation;

import org.cn.personalapi.domain.user.domain.PersonalType;
import org.cn.personalapi.domain.user.domain.User;

public class UserDto {

    public record InfoRes (
        Long id,
        String name,
        PersonalType personalColorType,
        String profileImageUrl
    ) {
        public InfoRes(User user) {
            this(
                    user.getId(),
                    user.getName(),
                    user.getPersonalType(),
                    user.getProfileImageUrl()
            );
        }
    }
}
