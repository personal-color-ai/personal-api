package org.cn.personalapi.domain.user.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PersonalType {
    SPRING("봄"),
    SUMMER("여름"),
    AUTUMN("가을"),
    WINTER("겨울");

    private final String value;

    public String getValue() {
        return value;
    }
}
