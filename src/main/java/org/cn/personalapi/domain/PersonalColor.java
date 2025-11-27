package org.cn.personalapi.domain;

import lombok.Getter;

@Getter
public enum PersonalColor {
    SPRING_WARM("봄 웜톤", "밝고 따뜻한 색상"),
    SUMMER_COOL("여름 쿨톤", "부드럽고 시원한 색상"),
    AUTUMN_WARM("가을 웜톤", "깊고 따뜻한 색상"),
    WINTER_COOL("겨울 쿨톤", "선명하고 차가운 색상");

    private final String koreanName;
    private final String description;

    PersonalColor(String koreanName, String description) {
        this.koreanName = koreanName;
        this.description = description;
    }
}
