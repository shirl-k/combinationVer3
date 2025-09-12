package com.example.combination.domain.order;


import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;

import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.exception.SKUNotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    @Column(nullable = false, length = 100)
    private String cartName; // 장바구니 이름 (예: "거실 인테리어", "침실 인테리어")
    
    @Column(length = 500)
    private String description; // 장바구니 설명
    
    @Column(nullable = false)
    private boolean isDefault; // 기본 장바구니 여부
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;   //서비스 옵션 선택 (배송만 서비스/ 이사서비스)

    @OneToOne(mappedBy = "shoppingCart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private MovingService movingService;

//===========핵심 비즈니스 로직============//ss

    //회원별 장바구니 생성 메서드
    public static ShoppingCart createCart(Member member, String cartName, String description) {
        return ShoppingCart.builder()
                .member(member)
                .cartName(cartName)
                .description(description)
                .isDefault(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    //기본 장바구니 생성 메서드
    public static ShoppingCart createDefaultCart(Member member) {
        return ShoppingCart.builder()
                .member(member)
                .cartName("기본 장바구니")
                .description("기본 장바구니입니다.")
                .isDefault(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    // 장바구니 정보 업데이트
    public void updateCartInfo(String cartName, String description) {
        this.cartName = cartName;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
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
        if (skuId == null) {
            throw new IllegalArgumentException("SKU ID는 null일 수 없습니다.");
        }
        
        CartItem target = cartItems.stream()
                .filter(cartItem -> cartItem != null && 
                                   cartItem.getSku() != null && 
                                   skuId.equals(cartItem.getSku().getSkuId()))
                .findFirst()
                .orElseThrow(() -> new SKUNotFoundException("해당 SKU 없음: " + skuId));
        removeItemFromCart(target);
    }

    public Optional<CartItem> findItemBySkuId(String skuId) {
        if (skuId == null) {
            return Optional.empty();
        }
        
        return cartItems.stream()
                .filter(cartItem -> cartItem != null && 
                                   cartItem.getSku() != null && 
                                   skuId.equals(cartItem.getSku().getSkuId()))
                .findFirst();
    }

    public void increaseQuantity(String skuId, int amount) {
        CartItem item = findItemBySkuId(skuId)
                .orElseThrow(() -> new SKUNotFoundException("장바구니에 해당 SKU가 없음.: " + skuId));
        item.setQuantity(item.getQuantity() + amount);
    }

    public void decreaseQuantity(String skuId, int amount) {
        CartItem item = findItemBySkuId(skuId)
                .orElseThrow(() -> new SKUNotFoundException("장바구니에 해당 SKU가 없음.: " + skuId));
        item.setQuantity(item.getQuantity() - amount);
        //수량 <=0 이면 카트 자동 삭제
        if (item.getQuantity() <= 0) {
            removeItemFromCart(item);
        }
    }
    //장바구니 총합 금액 (예상 지불 금액)
    public int calculateTotalPrice() {
        int itemsTotal = cartItems.stream()
                    .filter(CartItem::isSelected)
                    .mapToInt(CartItem::getTotalPrice) // unitPrice * quantity
                    .sum();
        if(serviceType == null) {
            throw new IllegalStateException("배송 옵션을 선택해주세요.");
        }
        if(itemsTotal <= 0) {
            throw new IllegalStateException("주문 상품을 최소 1개 담아주세요.");
        }

        return switch (serviceType) {
            case JUST_DELIVERY -> itemsTotal;   //할인 금액 적용 시 int finalPrice = Math.max(0, itemsTotal - memberDiscount); 추가. MemberDiscount 할인 정책 필요. 값 계산해서 필드 초기화
            case MOVING_SERVICE -> {
                // NPE 방지: movingService가 null인 경우 기본 배송비만 적용
                if (movingService == null) {
                    yield itemsTotal;
                }
                yield itemsTotal + movingService.calculateMovingServicePrice();
            }
        };
    }
    
    // ServiceType 설정 메서드 추가
    public void updateServiceType(ServiceType newServiceType) {
        this.serviceType = newServiceType;
        this.updatedAt = LocalDateTime.now();
        
        // MOVING_SERVICE 선택 시 MovingService가 없으면 기본값으로 생성
        if (newServiceType == ServiceType.MOVING_SERVICE && this.movingService == null) {
            MovingService movingService = MovingService.builder()
                    .shoppingCartId(this.cartId)
                    .movingServiceDescription("기본 이사 서비스")
                    .build();
            movingService.assignToCart(this.cartId);
            this.movingService = movingService;
        }
        // JUST_DELIVERY 선택 시 MovingService 제거
        else if (newServiceType == ServiceType.JUST_DELIVERY) {
            this.movingService = null;
        }
    }
}
//        MovingService.calculateMovingServicePrice() -> 가격 정책
//        if(movingService.calculateMovingServicePrice() < 0) {
//            throw new IllegalStateException("이사 서비스 비용이 유효하지 않습니다.");
//        }



    //개별 상품 수량 증가
//    public void increaseQuantity(CartItem cartItem, int quantity) {
//        cartItem.setQuantity(cartItem.getQuantity() + quantity);
//    }

    //개별 수량 감소
//    public void decreaseQuantity(CartItem cartItem, int quantity) {
//        cartItem.setQuantity(cartItem.getQuantity() - quantity);
//    }













