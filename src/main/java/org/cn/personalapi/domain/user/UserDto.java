package org.cn.personalapi.domain.user;

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
