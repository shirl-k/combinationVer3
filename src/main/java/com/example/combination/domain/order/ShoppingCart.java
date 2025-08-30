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
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private int unitPrice; //주문 당시 가격 (상품 가격 변동 고려)

    private int quantity; //주문 수량

    private int discountPrice; //할인금액 정가-단가

//===========핵심 비즈니스 로직============//

    //회원별 장바구니 생성 메서드
    public static ShoppingCart createCart(Member member) {
        return ShoppingCart.builder()
                .member(member)
                .build();
    }

    //연관관계 편의 메서드 - DB와 엔티티 양쪽이 동기화. 양방향 연관관계의 반대편(읽기 전용 컬렉션) 도 업데이트
    public void addItemToCart(CartItem cartItem) {
        cartItem.setShoppingCart(this); //장바구니에 CartItem 추가 cartItem → shoppingCart 연관관계의 주인(FK 컬럼)을 설정하는 부분
        cartItems.add(cartItem);
    }

    public void removeItemFromCart(CartItem cartItem) { //장바구니에서 CartItem 삭제
        cartItems.remove(cartItem);
        cartItem.setShoppingCart(null); //양방향 연관관계 해제를 의미. 객체 그래프 + DB 모두에서 관계가 일관성 있게 해제
    }

    //개별 상품 수량 증가
    public void increaseQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    //개별 수량 감소
    public void decreaseQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(cartItem.getQuantity() - quantity);
    }

    //할인금액 포함 총합 금액
    public int calculateTotal() { //실시간 계산 메서드
        return cartItems.stream()
                .filter(CartItem::isSelected)
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }
    //discountPrice 계산기
}








