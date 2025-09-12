package com.example.combination.domain.member;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.domain.valuetype.*;
import com.example.combination.exception.InsufficientPointsException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@Entity
@Table(name = "members")
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
    @Builder.Default
    private List<Order> orders = new ArrayList<>(); //매핑 안하면 빨간줄

    //valuetype 패키지

    @Embedded
    private UserInfo userInfo;

    @Embedded
    private HomeAddress homeAddress;
//
//    @Embedded
//    private DeliveryAddress deliveryAddress; // 배송지 주소

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();

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
        if (pointsToUse <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 0보다 커야 합니다: " + pointsToUse);
        }
        
        if (pointsToUse > this.availablePoints) {
            throw new InsufficientPointsException(pointsToUse, this.availablePoints);
        }
        
        this.availablePoints -= pointsToUse;
    }
    
    /**
     * 포인트 복구 (주문 취소 시 사용)
     */
    public void restorePoints(int pointsToRestore) {
        if (pointsToRestore <= 0) {
            throw new IllegalArgumentException("복구할 포인트는 0보다 커야 합니다: " + pointsToRestore);
        }
        this.availablePoints += pointsToRestore;
    }
    //현재 사용 가능 포인트
    public int getAvailablePoints() {
        return this.availablePoints;
    }

    //포인트 누적
    public void addMemberPoints(int newPoints) {
        this.availablePoints += newPoints;
    }
    
    // 장바구니 관련 메서드들
    public void addShoppingCart(ShoppingCart cart) {
        shoppingCarts.add(cart);
        cart.setMember(this);
    }
    
    public void removeShoppingCart(ShoppingCart cart) {
        shoppingCarts.remove(cart);
        cart.setMember(null);
    }
    
    public ShoppingCart getDefaultCart() {
        return shoppingCarts.stream()
                .filter(ShoppingCart::isDefault)
                .findFirst()
                .orElse(null);
    }
    
    public List<ShoppingCart> getNonDefaultCarts() {
        return shoppingCarts.stream()
                .filter(cart -> !cart.isDefault())
                .toList();
    }

}







