package com.example.combination.domain.movingService;

import com.example.combination.domain.common.OrderReference;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "moving_services")
public class MovingService implements OrderReference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 500)
    private String movingServiceDescription;

    private String name; //회원 이름

    private String phoneNum; //회원 전화번호

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "district", column = @Column(name = "home_district")),
            @AttributeOverride(name = "roadNameAddress", column = @Column(name = "home_road_name")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "home_zipcode")),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "home_detail_address"))
    })
    private HomeAddress homeAddress; //기존 집 주소

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "moving_city")),
            @AttributeOverride(name = "district", column = @Column(name = "moving_district")),
            @AttributeOverride(name = "roadNameAddress", column = @Column(name = "moving_road_name")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "moving_zipcode")),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "moving_detail_address"))
    })
    private MovingServiceAddress movingServiceAddress; //새 주소 (배송지)

    //이사 서비스
    @Enumerated(EnumType.STRING)
    private MovingServiceStatus movingServiceStatus;

    private LocalDateTime ready;

    private LocalDateTime shipped;

    private LocalDateTime inTransit;

    private LocalDateTime delivered;

    private LocalDateTime newSetting;

    private LocalDateTime home;

    private LocalDateTime transfer;

    private LocalDateTime newHome;


    private Long shoppingCartId; //cartId랑 컬럼명 동일해서 변경

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long orderCode; //OrderCode
    
    @Transient
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * OrderReference 인터페이스 구현
     */
    @Override
    public Long getOrderId() {
        return orderCode;
    }
    
    @Override
    @Enumerated(EnumType.STRING) //@@
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    
    /**
     * 주문과의 연관관계 설정 (순환참조 방지)
     */
    public void assignToOrder(Long orderId, OrderStatus orderStatus) {
        this.orderCode = orderId;
        this.orderStatus = orderStatus;
    }
    
    /**
     * 주문과의 연관관계 해제
     */
    public void detachFromOrder() {
        this.orderCode = null;
        this.orderStatus = null;
    }
    
    /**
     * 장바구니와의 연관관계 설정
     */
    public void assignToCart(Long cartId) {
        this.shoppingCartId = shoppingCart.getCartId();
    }
    
    /**
     * 장바구니와의 연관관계 해제
     */
    public void detachFromCart() {
        this.shoppingCartId = null;
    }

    public void changeMovingServiceStatus(MovingServiceStatus newStatus) {
        switch (newStatus) {
            case READY:
                this.ready = LocalDateTime.now();
            case SHIPPED:
                this.shipped = LocalDateTime.now();
            case IN_TRANSIT:
                this.inTransit = LocalDateTime.now();
            case DELIVERED:
                this.delivered = LocalDateTime.now();
            case NEW_SETTING:
                this.newSetting = LocalDateTime.now();
            case HOME:
                this.home = LocalDateTime.now();
            case TRASFER:
                this.transfer = LocalDateTime.now();
            case NEWHOME:
                this.newHome = LocalDateTime.now();
        }
    }

    public int calculateMovingServicePrice() {
        return 500000; // 고정 이사 서비스 비용 : 50만원. 이사 비용 정책 추가해야함.
    }
}
