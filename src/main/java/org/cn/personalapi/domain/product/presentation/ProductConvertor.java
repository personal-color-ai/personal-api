// src/main/java/org/cn/personalapi/domain/product/presentation/ProductConvertor.java

package org.cn.personalapi.domain.product.presentation;

import org.cn.personalapi.domain.product.domain.Option;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.domain.product.dto.OptionDto;
import org.cn.personalapi.domain.product.dto.ProductDto;
import org.cn.personalapi.global.PageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProductConvertor {

    // Page 변환 지원
    public static PageDto.Dto toPageListRes(Page<Product> products) {
        List<ProductDto.ListRes> plist = products.stream()
                .map(product -> new ProductDto.ListRes(
                        product.getId(),
                        product.getName(),
                        product.getBrand(),
                        product.getRating(),
                        product.getImageUrl()
                )).toList();

        return PageDto.Dto.builder()
                .list(plist)
                .listSize(products.getSize())
                .totalPage(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .isFirst(products.isFirst())
                .isLast(products.isLast())
                .build();
    }

    public static ProductDto.DetailRes toDetailRes(Product product) {
        // ... (기존 코드 유지)
        return new ProductDto.DetailRes(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getRating(),
                product.getPrice(),
                product.getUrl(),
                product.getImageUrl(),
                product.getFeatures(),
                product.getReviews() != null ? product.getReviews().size() : 0,
                countReviewsByRating(product, 1),
                countReviewsByRating(product, 2),
                countReviewsByRating(product, 3),
                countReviewsByRating(product, 4),
                countReviewsByRating(product, 5),
                toOptionDetailRes(product.getOptions())
        );
    }

    private static Integer countReviewsByRating(Product product, int rating) {
        if (product.getReviews() == null) return 0;
        return Math.toIntExact(product.getReviews().stream()
                .filter(r -> r.getRating() == rating)
                .count());
    }

    private static List<OptionDto.DetailRes> toOptionDetailRes(List<Option> option) {
        if (option == null) return List.of();
        return option.stream().map(opt -> new OptionDto.DetailRes(
                opt.getId(),
                opt.getName(),
                opt.getImageUrl(),
                opt.getOptionNo()
        )).toList();
    }
}