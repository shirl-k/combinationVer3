package com.example.combination.domain.order;

import com.example.combination.domain.delivery.*;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.exception.OrderStatusTransitionException;
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
@Table(name = "orders")
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
            validateOrderStatusTransition(this.orderStatus, orderStatus);
            this.orderStatus = orderStatus;
        }
        
        /**
         * 주문 상태 전환 검증
         */
        private void validateOrderStatusTransition(OrderStatus fromStatus, OrderStatus toStatus) {
            if (fromStatus == null) {
                return; // 최초 생성 시에는 검증하지 않음
            }
            
            // 허용되는 상태 전환 규칙
            switch (fromStatus) {
                case CREATED:
                    if (toStatus != OrderStatus.CONFIRMED && toStatus != OrderStatus.CANCELLED) {
                        throw new OrderStatusTransitionException(fromStatus, toStatus);
                    }
                    break;
                case CONFIRMED:
                    if (toStatus != OrderStatus.COMPLETED && toStatus != OrderStatus.CANCELLED) {
                        throw new OrderStatusTransitionException(fromStatus, toStatus);
                    }
                    break;
                case COMPLETED:
                    // 완료된 주문은 더 이상 변경할 수 없음
                    throw new OrderStatusTransitionException(fromStatus, toStatus);
                case CANCELLED:
                    // 취소된 주문은 더 이상 변경할 수 없음
                    throw new OrderStatusTransitionException(fromStatus, toStatus);
                default:
                    throw new OrderStatusTransitionException(fromStatus, toStatus);
            }
        }

        //장바구니 총합 금액 (예상 지불 금액)  -> OrderService 로 이동. Order는 주문 상태만 관리
//        public int calculateLineTotalPrice() {
//        int basePrice = orderItems.stream()
//                .filter(OrderItem::isSelected)
//                .mapToInt(OrderItem::getLineTotal) //unitPrice * quantity
//                .sum();
//
//        int servicePrice = 0; //서비스 비용 초기화
//
//        switch (this.serviceType) { //서비스 유형에 따라 다른 비용 추가
//            case JUST_DELIVERY:
//                if (this.justDelivery != null) { // JustDelivery 엔티티가 존재할 때만 비용을 계산
//                    servicePrice = this.justDelivery.calculateDeliveryPrice();
//                }
//                break;
//            case MOVING_SERVICE:
//                if (this.movingService != null) { // MovingService 엔티티가 존재할 때만 비용을 계산
//                    servicePrice = this.movingService.calculateMovingServicePrice();
//                }
//                break;
//        }
//
//        this.totalAmount = basePrice + servicePrice;
//        return this.totalAmount;
//    }

        //상품 총액 계산
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

        this.totalAmount = itemsTotal + servicePrice;
        return this.totalAmount;
    }

        //포인트 적용 최종 금액 계산
        public void applyPoints() {
            if (usePoints) {
                int availablePoints = member.getAvailablePoints();
                this.usedPoints = Math.min(totalAmount, availablePoints);
                this.finalPrice = totalAmount - this.usedPoints;
            } else {
                this.finalPrice = totalAmount;
                this.usedPoints = 0;
            }
        }

        //===========연관관계 편의 메서드 =================//orderItem 과 Order 양쪽 동일하게 업데이트
        public void addOrderItem(OrderItem orderItem) {
            orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
        //주문 취소 OrderStatus - CANCELLED
        public void removeOrderItem(OrderItem orderItem) {
            // null 체크 추가
            if (orderItem != null) {
                orderItems.remove(orderItem);
                orderItem.setOrder(null);
            }
        }
        
        /**
         * 배송서비스 할당 (순환참조 방지)
         */
        public void setJustDelivery(JustDelivery justDelivery) {
            this.justDelivery = justDelivery;
            if (justDelivery != null) {
                justDelivery.assignToOrder(this.orderId, this.orderStatus);
            }
            this.movingService = null;
        }
        
        /**
         * 이사서비스 할당 (순환참조 방지)
         */
        public void setMovingService(MovingService movingService) {
            this.movingService = movingService;
            if (movingService != null) {
                movingService.assignToOrder(this.orderId, this.orderStatus);
            }
            this.justDelivery = null;
        }

        //============팩토리 메서드==============//
        public static Order createOrder(Member member, List<OrderItem> orderItems, ServiceType serviceType, OrderStatus orderStatus, boolean usePoints, int usedPoints) {
            Order order = Order.builder()
                    .member(member)
                    .serviceType(serviceType)
                    .orderStatus(orderStatus)
                    .usePoints(usePoints)
                    .usedPoints(usedPoints)
                    .createOrderDate(LocalDate.now())
                    .build();

            orderItems.forEach(order::addOrderItem); // orderItem 추가 시에도 양방향 관계 보장 : builder로 넣으면 양방향 관계 깨질 수 있으니 for문 돌면서 addOrderItem() 호출

            return order;
        }

}