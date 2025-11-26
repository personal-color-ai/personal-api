package org.cn.personalapi.domain.product.dto;

import org.cn.personalapi.domain.product.domain.Category;

public class CrawlingDto {

    public record BeautyReq(
        Category category
    ) {}

    public record FastBeautyReq(
        String category
    ) {}
}
