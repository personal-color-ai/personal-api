package org.cn.personalapi.domain.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    long countByUserDescriptionContaining(String searchKeyword);

}
