package org.cn.personalapi.domain.product.repository;

import org.cn.personalapi.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByGoodsId(String goodsId);

    // 크롤링 중복 검사
    List<Product> findAllByGoodsIdIn(List<String> goodsIds);

    // 리뷰/옵션이 없는 상품만 조회 (메모리 필터링 대신 DB 필터링)
    List<Product> findByReviewsIsEmpty();
    List<Product> findByOptionsIsEmpty();
}
