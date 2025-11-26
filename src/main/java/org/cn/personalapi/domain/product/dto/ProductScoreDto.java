package org.cn.personalapi.domain.product.dto;

import org.cn.personalapi.domain.product.domain.Product;


public class ProductScoreDto {
    private final Product product;
    private final double score;

    public ProductScoreDto(Product product, double score) {
        this.product = product;
        this.score = score;
    }

    public Product getProduct() {
        return product;
    }

    public double getScore() {
        return score;
    }
}