package org.cn.personalapi.domain.product.presentation;

public class OptionDto {
    public record DetailRes (
        Long id,
        String name,
        String imageUrl,
        String optionNo
    ) {}
}
