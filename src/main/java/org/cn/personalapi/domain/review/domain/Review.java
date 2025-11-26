package org.cn.personalapi.domain.review.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cn.personalapi.domain.product.domain.Product;
import org.cn.personalapi.global.res.AuditingEntity;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String userName;
    private String userDescription;
    private String userImage;

    private Integer rating;      // 별점 (예: 5)
    private Integer likes;       // 도움돼요 수
    @Lob
    private String content;      // 본문
}