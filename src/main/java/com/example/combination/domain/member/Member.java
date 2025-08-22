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
    @Id
    @GeneratedValue
    private Long id; //자동생성 id

    private String name;

    @Enumerated(EnumType.STRING)
    private Membership membershipGrade;

    //회원별 주문 목록
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); //매핑 안하면 빨간줄

    //valuetype 패키지

    @Embedded
    private UserInfo userInfo;

    @Embedded
    @Column(nullable = false)
    private HomeAddress homeAddress;

    @Embedded
    @Column(nullable = false)
    private DelivAddress delivAddress; // 배송지 주소

    private SocialAccount socialAccount;

    private ShoppingCart shoppingCart;

}

    //=======================================//
//    private String shoppingCart;
//
//    @Embedded
//    private RelSocialAccount relSocialAccount;

    //======================================//

//    private List<RelSocialAccount> relSocialAccounts = new ArrayList<>();

//    private List<DelivAddress> delivAddresses = new ArrayList<>()




