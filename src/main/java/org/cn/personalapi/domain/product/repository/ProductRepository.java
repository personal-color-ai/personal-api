package org.cn.personalapi.domain.product.repository;

import org.cn.personalapi.domain.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 크롤링 중복 검사
    List<Product> findAllByGoodsIdIn(List<String> goodsIds);

    // 리뷰/옵션이 없는 상품만 조회 (메모리 필터링 대신 DB 필터링)
    List<Product> findByReviewsIsEmpty();
    List<Product> findByOptionsIsEmpty();


    /*
     * [베이지안 평균 정렬]
     * 단순 평균의 문제(리뷰 1개, 평점 5점 = 1등)를 보정하기 위한 알고리즘.
     * 데이터(리뷰)가 적을수록 '전체 평균'에 수렴하고, 많을수록 '실제 평점'에 가까워집니다.
     *
     * 공식: (Match + m*C) / (v + m)
     * - Match: 내 체질(PersonalType)과 일치하는 리뷰 수
     * - v : 해당 상품의 총 리뷰 수
     * - m : 가중치를 위한 최소 리뷰 수 (가중치 상수)
     * - C : 모든 상품의 평균 매칭 확률 (사전 확률)
     */
    @Query(value = """
        SELECT p.*
        FROM product p
        LEFT JOIN review r ON p.id = r.product_id
        GROUP BY p.id
        ORDER BY (
            (COUNT(r.id) / (COUNT(r.id) + :minReviews)) * (SUM(CASE WHEN r.user_description LIKE %:keyword% THEN 1 ELSE 0 END) / NULLIF(COUNT(r.id), 0)) 
            + 
            (:minReviews / (COUNT(r.id) + :minReviews)) * :overallAvg
        ) DESC
        """,
            countQuery = "SELECT count(*) FROM product",
            nativeQuery = true)
    Page<Product> findProductsSortedByBayesianScore(
            @Param("keyword") String keyword,
            @Param("overallAvg") double overallAvg,
            @Param("minReviews") int minReviews,
            Pageable pageable
    );
}
