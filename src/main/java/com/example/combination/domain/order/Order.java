package com.example.combination.domain.order;

import com.example.combination.domain.delivery.*;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.payment.PaymentMethod;
import com.example.combination.domain.valuetype.DeliveryAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

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
        private MembershipGrade membershipGrade;


        @Enumerated(EnumType.STRING)
        private PaymentMethod paymentMethod;


        @Enumerated(EnumType.STRING)
        private OrderStatus orderStatus; //주문 자체 흐름. 주문의 큰 그림만 관리 (결제가 실패했다고 해서 Order를 바로 실패로 두진 않음. 아직 살아있을 수 있음.)
                                            // CREATED(주문 생성됨. 결제 전), CONFIRMED(결제 성공, 주문 확정), CANCELLED(결제 전 취소), COMPLETED(배송까지 완료)

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> orderItems = new ArrayList<>();


        private LocalDate createOrderDate; //주문 생성 시점


    //=========================================================//

        //각각의 서비스 폼 ( 둘 중 하나만 존재할 수 있음)
        @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private JustDelivery justDelivery;

        @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private MovingService movingService;
    //==========================================================//
//        @Embedded
//        private HomeAddress homeAddress;
//
//        @Embedded
//        private MovingServiceAddress movingServiceAddress;
//
//        @Embedded
//        private DeliveryAddress deliveryAddress;
//

        @Enumerated(EnumType.STRING)
        private ServiceType serviceType;

//        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//        private SelectedServiceType selectedServiceType;

        private int basePrice; //배송서비스 (추가 배송비 없음)

   //==========================================================//
//        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
//        private DeliveryAddressForm deliveryAddressForm;

        @Column(length = 500)
        private String justDeliveryDescription;

        @Column(length = 500)
        private String movingServiceDescription;

        //==================================================//

        private boolean usePoints;

        private int finalPrice;

        private int usedPoints;

        //============핵심 비즈니스 로직==============//

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
        //==============================================//
        //private boolean movingService;

//        public void setDeliveryForm( //MovingServiceForm movingServiceForm) {
//               // if(movingService) { //이사 서비스 이용 시
//                    if (movingServiceForm == null) {
//                        throw new IllegalStateException("이사 서비스 이용 시 현 주소지와 배송지 주소 입력이 필수입니다.");
//                    }//기존 주소와 새 주소, 배송 요청사항
//                   // this.homeAddress = movingServiceForm.getHomeAddress();
//                    //this.movingServiceAddress = movingServiceForm.getMovingServiceAddress();
//                    //this.movingServiceDescription = movingServiceForm.getMovingServiceDescription();
//
//                    // 홈/비즈니스용 배송만 서비스 필드 초기화
//                    this.deliveryAddress = null;
//                    this.deliveryDescription = null;
//
//                } else { //홈/비즈니스 용 서비스 이용 시
//                    if (deliveryForm == null) {
//                        throw new IllegalStateException("배송지 입력은 필수입니다.");
//                    }
//                    //배송지와 배송지 옵션
//                    this.deliveryAddress = deliveryForm.getDeliveryAddress();
//                    this.deliveryDescription = deliveryForm.getDeliveryDescription();
//
//                    //이사 서비스 필드 초기화
//                    this.movingServiceAddress = null;
//                    this.movingServiceDescription = null;
//                }
//        }

//        public void setMovingServiceForm(MovingServiceForm entity) {
//            this.movingServiceAddress = entity.getMovingServiceAddress();
//            this.homeAddress = entity.getHomeAddress();
//            this.movingServiceDescription = entity.getMovingServiceDescription();
//        }

        //결제 방식
        public void selectPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        //총 결제 금액
        public int calculateLineTotalPrice() {
            return orderItems.stream()
                    .filter(OrderItem::isSelected)
                    .mapToInt(OrderItem::getLineTotal) //unitPrice * quantity
                    .sum();
        }
//        public int calculateLineTotalPrice() {
//            if (movingService == false) {
//                return orderItems.stream()
//                        .filter(OrderItem::isSelected)
//                        .mapToInt(OrderItem::getLineTotal) // unitPrice * quantity
//                        .sum();
//            } else {
//                return orderItems.stream()
//                        .filter(OrderItem::isSelected)
//                        .mapToInt(OrderItem::getLineTotal) // unitPrice * quantity
//                        .sum() + movingServicePrice ;
//            }
//        }


        //포인트 사용 여부 - 포인트 차감된 결제 금액
        public void calculateFinalPrice(Member member) {
            int total = calculateLineTotalPrice();

            if(usePoints) {
                int availablePoints = member.getAvailablePoints();
                this.usedPoints = Math.min(total, availablePoints); //Math.min
                this.finalPrice = total - this.usedPoints;
            } else {
                this.finalPrice = total;
                this.usedPoints = 0;
            }
        }
        public int getFinalPrice() {
            return finalPrice;
        }

        public int getUsedPoints() {
            return usedPoints;
        }
        //최종 주문 스냅샷

    public static Order createFinalOrder(Member member, List<OrderItem> orderItems, PaymentMethod paymentMethod
            , JustDelivery justDelivery, OrderStatus orderStatus, boolean usePoints, int usedPoints) {
        Order order = Order.builder()
                .member(member)
                .membershipGrade(member.getMembershipGrade())
                .orderStatus(OrderStatus.CREATED)
                .orderItems(new ArrayList<>(orderItems))
                .usePoints(usePoints)
                .justDelivery(justDelivery)
                //.deliveryForm(deliveryForm)
                .paymentMethod(paymentMethod)
                .createOrderDate(LocalDate.now())
                .usedPoints(usedPoints)
                .build();

        orderItems.forEach(order::addOrderItem); // orderItem 추가 시에도 양방향 관계 보장 : builder로 넣으면 양방향 관계 깨질 수 있으니 for문 돌면서 addOrderItem() 호출
        return order;
    }

        //Order 변경감지
        public void changeOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }
}

