package com.example.combination.domain.member;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.domain.valuetype.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "member")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //자동생성 id

    private String name;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

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

    // === 연관관계 편의 메서드 ===
//    public void createShoppingCart() {
//        ShoppingCart cart = new ShoppingCart();
//        cart.setMember(this);      // cart.member = this
//        this.shoppingCart = cart;  // member.shoppingCart = cart
//    }

}






