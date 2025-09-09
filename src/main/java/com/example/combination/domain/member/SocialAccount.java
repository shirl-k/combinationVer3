package com.example.combination.domain.member;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "social_account")
public class SocialAccount {

    @Id @GeneratedValue
    @Column(name = "social_account_id")
    private Long id;

    @Column(nullable = false)
    private String provider; // google, naver, kakao

    @Column(nullable = false)
    private String providerId; // 소셜 로그인 제공자의 사용자 ID

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
