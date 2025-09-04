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
    
    private int totalSpent; //누적 금액

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

    private int availablePoints; //사용 가능 포인트(누적)
    private int usedPoints;
    private int usePoints;

    // ======비즈니스 로직========//

    //Member 변경감지 (회원가입/회원탈퇴)
    public void changeMemberStatus(MemberStatus newStatus) {
        this.memberStatus = newStatus;
    }
    // (로그인/로그아웃)
    public void changeLogInStatus(LogInStatus newStatus) {
        this.logInStatus = newStatus;
    }

    //누적 결제 금액
    public void addTotalSpent(int amount) {
        this.totalSpent += amount; //amount: 총액
        this.membershipGrade = MembershipGrade.calculateGrade(this.totalSpent);
        
    }

    //포인트 사용 시 차감
    public void usePoints(int pointsToUse) {
        if (pointsToUse > this.availablePoints) {
            throw new IllegalArgumentException("보유 포인트를 초과했습니다."); //
        }
        this.availablePoints -= pointsToUse;

    }
    //현재 사용 가능 포인트
    public int getAvailablePoints() {
        return this.availablePoints;
    }

    //포인트 누적
    public void addMemberPoints(int newPoints) {
        this.availablePoints += newPoints;
    }

}







