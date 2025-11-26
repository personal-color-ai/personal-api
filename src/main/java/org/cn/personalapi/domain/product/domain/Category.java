package org.cn.personalapi.domain.product.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    BASE("104014"),
    SKIN("104001"),
    EYE("104016"),
    LIP("104015");

    private final String code;

    public String getCode() {
        return code;
    }
}
