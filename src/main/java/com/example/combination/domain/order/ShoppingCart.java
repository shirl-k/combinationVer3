package com.example.combination.domain.order;


import com.example.combination.domain.member.Member;

import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.exception.SKUNotFoundException;
import jakarta.persistence.*;
import lombok.*;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class ShoppingCart { //실시간 계산 로직
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    private int memberDiscount;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();


//===========핵심 비즈니스 로직============//ss

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

    //skuId로 바로 삭제  - 도메인 편의 메서드
    public void removeItemBySkuId(String skuId) {
        CartItem target = cartItems.stream().filter(cartItem -> cartItem.getSku().getSkuId().equals(skuId))
                .findFirst()
                .orElseThrow(()-> new SKUNotFoundException("해당 SKU 없음: " + skuId));
        removeItemFromCart(target);
    }

    public Optional<CartItem> findItemBySkuId(String skuId) {
        return cartItems.stream().filter(cartItem -> cartItem.getSku().getSkuId().equals(skuId))
                .findFirst();
    }
    public void increaseQuantity(String skuId, int amount) {
        CartItem item = findItemBySkuId(skuId)
                .orElseThrow(()->new SKUNotFoundException("장바구니에 해당 SKU가 없음.: " + skuId));
        item.setQuantity(item.getQuantity() + amount);
    }
    public void decreaseQuantity(String skuId, int amount) {
        CartItem item = findItemBySkuId(skuId)
                .orElseThrow(()->new SKUNotFoundException("장바구니에 해당 SKU가 없음.: " + skuId));
        item.setQuantity(item.getQuantity() - amount);
        //수량 <=0 이면 카트 자동 삭제
        if (item.getQuantity() <= 0) {
            removeItemFromCart(item);
        }
    }
    //장바구니 총합 금액
    public int calculateTotalPrice() {
        return cartItems.stream()
                .filter(CartItem::isSelected)
                .mapToInt(CartItem::getTotalPrice) // unitPrice * quantity
                .sum()-memberDiscount;
    }

}
    //개별 상품 수량 증가
//    public void increaseQuantity(CartItem cartItem, int quantity) {
//        cartItem.setQuantity(cartItem.getQuantity() + quantity);
//    }

    //개별 수량 감소
//    public void decreaseQuantity(CartItem cartItem, int quantity) {
//        cartItem.setQuantity(cartItem.getQuantity() - quantity);
//    }













