package org.cn.personalapi.domain.product;

import java.util.ArrayList;
import java.util.List;

public class ProductConvertor {
    public static List<ProductDto.ListRes> toListResList(List<Product> products) {
        List<ProductDto.ListRes> resList = new ArrayList<>();
        for (Product product : products) {
            ProductDto.ListRes res = new ProductDto.ListRes(
                    product.getId(),
                    product.getName(),
                    product.getBrand(),
                    product.getRating(),
                    product.getImageUrl()
            );
            resList.add(res);
        }
        return resList;
    }

    public static ProductDto.DetailRes toDetailRes(Product product) {
        return new ProductDto.DetailRes(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getRating(),
                product.getPrice(),
                product.getImageUrl(),
                product.getFeatures(),
                // 리뷰
                product.getUrl(),
                product.getReviews().size(),
                countReviewsByRating(product, 1),
                countReviewsByRating(product, 2),
                countReviewsByRating(product, 3),
                countReviewsByRating(product, 4),
                countReviewsByRating(product, 5),
                toOptionDetailRes(product.getOptions())
        );
    }

    private static Integer countReviewsByRating(Product product, int rating) {
        return Math.toIntExact(product.getReviews().stream()
                .filter(r -> r.getRating() == rating)
                .count());
    }

    private static List<OptionDto.DetailRes> toOptionDetailRes(List<Option> option) {
        return option.stream().map(opt -> new OptionDto.DetailRes(
                opt.getId(),
                opt.getName(),
                opt.getImageUrl(),
                opt.getOptionNo()
        )).toList();
    }
}
