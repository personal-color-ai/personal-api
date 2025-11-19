package org.cn.personalapi.domain.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.cn.personalapi.domain.product.domain.Option;
import org.cn.personalapi.domain.review.domain.Review;
import org.cn.personalapi.global.AuditingEntity;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product")
public class Product extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goodsId;     // 무신사 상품 고유 아이디
    private String name;         // 프리즘 하이라이터
    private String brand;        // 클리오
    private Integer price;       // 14000
    private Double rating;       // 4.8
    private Integer reviewCount; // 256
    private String url;         // 무신사 상품 URL
    private String imageUrl;     // 대표 이미지 URL
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;  // 제품 설명
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String features;     // 주요 특징 (문장 전체 저장)

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @BatchSize(size = 30)
    private List<Option> options;
}
