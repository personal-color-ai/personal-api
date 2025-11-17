package org.cn.personalapi.domain.product.presentation;

import java.util.List;

public class ProductDto {

    public record ListRes (
        Long id,
        String name,
        String brand,
        Double rating,
        String imageUrl
    ) {}

    public record DetailRes (
        Long id,
        String name,
        String brand,
        String description,
        Double rating,
        Integer price,
        String originUrl,
        String imageUrl,
        String features,
        Integer reviewCountAll,
        Integer reviewCountOne,
        Integer reviewCountTwo,
        Integer reviewCountThree,
        Integer reviewCountFour,
        Integer reviewCountFive,
        List<OptionDto.DetailRes> options
    ) {}

    public record ReviewReq (
            Integer perCount
    ) {}
}
