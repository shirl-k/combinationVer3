package com.example.combination.domain.order;

import com.example.combination.domain.member.Member;

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
@Table(name = "shoppingCart")
public class ShoppingCart {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int orderPrice; //주문 당시 가격 (상품 가격 변동 고려)

    private int orderQuantity; //주문 수량


    //------------//
    public ShoppingCart createCart(Member member) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setMember(member);
        return shoppingCart;
    }

    public int getTotalPrice() {
        return orderQuantity * orderPrice;  //쇼핑카트에서 합산
    }


    //-------------------------------------------- totalPrice 주문서 총합금액
//    int totalPrice = 0;
//
//    for(
//    OrderItem sum :orderItems)
//
//    {
//        totalPrice += sum.getTotalPrice();
//    } return totalPrice;

//------------------------------------------------/

//    private TotalPrice totalPrice;
//    private int discountPrice;

    // setter 대신 안전하게 연관관계 편의 메서드 사용
//    public void setMember(Member member) {
//        this.member = member;
//    }

/*    //장바구니 자동생성 연관관계 편의 메서드 사용 예시
    Member member = new Member();

   // 멤버 생성 시 자동으로 장바구니도 같이 생성
    member.createShoppingCart();

    entityManager.persist(member);
// cascade = CascadeType.ALL 때문에 ShoppingCart도 자동 저장됨

 */


}
