package com.example.combination.domain.member;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.domain.valuetype.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //자동생성 id    //PK

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String userId;   //회원 계정 로그인 키

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;  //회원가입(계정 생성)/회원탈퇴

    @Enumerated(EnumType.STRING)
    private LogInStatus logInStatus;//로그인 성공/로그인 실패/로그아웃

    //회원별 주문 목록
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); //매핑 안하면 빨간줄

    //valuetype 패키지

    @Embedded
    private UserInfo userInfo;

    @Embedded
    private HomeAddress homeAddress;

    @Embedded
    private DelivAddress delivAddress; // 배송지 주소

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_account_id")
    private SocialAccount socialAccount;

    @OneToOne(mappedBy = "member",fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;

    // ======비즈니스 로직========//

    //Member 변경감지
    public void changeMemberStatus(MemberStatus newStatus) {
        this.memberStatus = newStatus;
    }

}






