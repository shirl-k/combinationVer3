package com.example.combination.domain.order;

import com.example.combination.domain.delivery.*;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "order")
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id")
        private Long orderId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id")
        private Member member;


        @Enumerated(EnumType.STRING)
        private ServiceType serviceType;

        @Enumerated(EnumType.STRING)
        private com.example.combination.domain.payment.PaymentMethod paymentMethod;

        @Enumerated(EnumType.STRING)
        private OrderStatus orderStatus; //주문 자체 흐름. 주문의 큰 그림만 관리 (결제가 실패했다고 해서 Order를 바로 실패로 두진 않음. 아직 살아있을 수 있음.)
                                            // CREATED(주문 생성됨. 결제 전), CONFIRMED(결제 성공, 주문 확정), CANCELLED(결제 전 취소), COMPLETED(배송까지 완료)

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<OrderItem> orderItems = new ArrayList<>();

        private int totalAmount; //상품 총액 + 서비스 비용

        private int finalPrice; // 포인트까지 적용된 최종 금액

        private int usedPoints; //사용한 포인트

        private boolean usePoints;


    //=========================================================//

        //각각의 서비스 폼 ( 둘 중 하나만 존재할 수 있음)
        @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private JustDelivery justDelivery;

        @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private MovingService movingService;
    //==========================================================//
        private LocalDate createOrderDate; //주문 생성 시점


        private int itemsTotal; // 상품 총합 금액 (현재는 배송서비스 선택 시 총 금액과 동일)

   //==========================================================//


        //============핵심 비즈니스 로직==============//
        //Order 변경감지
        public void changeOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

        //장바구니 총합 금액 (예상 지불 금액)  -> OrderService 로 이동. Order는 주문 상태만 관리
//        public int calculateLineTotalPrice() {
//        int basePrice = orderItems.stream()
//                .filter(OrderItem::isSelected)
//                .mapToInt(OrderItem::getLineTotal) //unitPrice * quantity
//                .sum();
//        if(basePrice <=0) {
//            throw new IllegalStateException("주문 상품을 최소 1개 담아야합니다.");
//        }
//        return switch (serviceType) {
//            case JUST_DELIVERY -> basePrice; //basePrice - memberDiscount 값이 음수가 될 수 있는 경우 고려해서 memberDiscount 초기화해야함
//            case MOVING_SERVICE -> basePrice + movingService.calculateMovingServicePrice(); //memberDiscount 따로 먼저 계산 후 차감
//        };
//    }


    //===========연관관계 편의 메서드 =================//orderItem 과 Order 양쪽 동일하게 업데이트
        public void addOrderItem(OrderItem orderItem) {
            orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
        //주문 취소 OrderStatus - CANCELLED
        public void removeOrderItem(OrderItem orderItem) {
            orderItems.remove(orderItem);
            orderItem.setOrder(null);
        }
        
        //JustDelivery 연관관계 편의 메서드
        public void setJustDelivery(JustDelivery form) {
            this.justDelivery = form;
            if(form != null) form.setOrder(this);
            //상호 배타성 보장
            this.movingService = null;
            this.serviceType = ServiceType.JUST_DELIVERY;
        }

        //MovingService 연관관계 편의 메서드
        public void setMovingService(MovingService form) {
            this.movingService = form;
            if(form != null) form.setOrder(this);
            //상호 배타성 보장
            this.justDelivery = null;
            this.serviceType = ServiceType.MOVING_SERVICE;
        }

        public int calculateLineTotalPrice() {
        //상품 총액 계산
        int itemsTotal = orderItems.stream()
                .mapToInt(OrderItem::getLineTotal)
                .sum();

        int servicePrice = 0; //서비스 비용 초기화

        // NPE 방지: 주문 엔티티의 serviceType이 null이 아닐 때만 로직을 실행
        if (this.serviceType != null) {
            switch (this.serviceType) { //서비스 유형에 따라 다른 비용 추가
                case JUST_DELIVERY:
                    if (this.justDelivery != null) { // JustDelivery 엔티티가 존재할 때만 비용을 계산
                        servicePrice = this.justDelivery.calculateDeliveryPrice();
                    }
                    break;
                case MOVING_SERVICE:
                    if (this.movingService != null) { // MovingService 엔티티가 존재할 때만 비용을 계산
                        servicePrice = this.movingService.calculateMovingServicePrice();
                    }
                    break;
            }
        }
        return this.totalAmount = itemsTotal + servicePrice;
    }

    private int pointsToUse;

    //포인트 적용 금액
    public void applyPoints() {
        if(usePoints) {
            int pointsToUse = Math.min(this.pointsToUse, this.member.getAvailablePoints());
            this.finalPrice = this.totalAmount - pointsToUse;
            this.usedPoints = pointsToUse;
        }
        else {
            this.finalPrice = this.totalAmount;
            this.usedPoints = 0;
        }
    }



        //최종 주문 스냅샷

    public static Order createOrder(Member member, List<OrderItem> orderItems
            , ServiceType serviceType, OrderStatus orderStatus, boolean usePoints, int usedPoints) {
        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.CREATED)
                .orderItems(new ArrayList<>(orderItems))
                .usePoints(usePoints)
                .serviceType(serviceType)
                .createOrderDate(LocalDate.now())
                .usedPoints(usedPoints)
                .build();

        orderItems.forEach(order::addOrderItem); // orderItem 추가 시에도 양방향 관계 보장 : builder로 넣으면 양방향 관계 깨질 수 있으니 for문 돌면서 addOrderItem() 호출
        return order;
    }


}

