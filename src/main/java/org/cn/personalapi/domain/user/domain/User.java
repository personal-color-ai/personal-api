package org.cn.personalapi.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cn.personalapi.global.AuditingEntity;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;             // 김민준
    private String email;            // 선택: 로그인용
    private String profileImageUrl;  // 프로필 이미지 URL

    private boolean premium;         // 프리미엄 사용자 여부

    @Enumerated(EnumType.STRING)
    private PersonalType personalType; // 퍼스널 컬러 타입

    // 통계
    private Integer analyzedItemCount;   // 분석한 아이템 수 (4)
    private Double matchRate;            // 평균 일치도 (89%)
    private Integer usageDays;           // 사용 일수 (7)

    // 알림 설정
    private Boolean notificationEnabled; // 알림 설정 여부
}
