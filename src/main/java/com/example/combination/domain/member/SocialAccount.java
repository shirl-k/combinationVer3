package com.example.combination.domain.member;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "social_account")
public class SocialAccount {

    @Id @GeneratedValue
    @Column(name = "social_account_id")
    private Long id;

    private String accoutInfo;

    @OneToOne(mappedBy = "socialAccount",fetch = FetchType.LAZY) //mappedBy ="클래스 안의 필드명 그대로"
    private Member member;
}
