package com.example.combination.domain.order;



import com.example.combination.domain.delivery.DeliveryAddressForm;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.payment.PaymentData;
import com.example.combination.domain.payment.PaymentMethod;

import com.example.combination.domain.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    private int memberPoints; //

    @OneToOne(fetch = FetchType.LAZY)
    private PaymentData paymentData;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 자체 흐름. 주문의 큰 그림만 관리 (결제가 실패했다고 해서 Order를 바로 실패로 두진 않음. 아직 살아있을 수 있음.)
                                        // CREATED(주문 생성됨. 결제 전), CONFIRMED(결제 성공, 주문 확정), CANCELLED(결제 전 취소), COMPLETED(배송까지 완료)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    private LocalDate createOrderDate; //주문 생성 시점

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DeliveryAddressForm deliveryAddressForm;

    private boolean usePoints;
    @Getter
    private int finalPrice; //최종 결제 금액
    @Getter
    private int usedPoints; //사용된 포인트

    //============핵심 비즈니스 로직==============//

    //연관관계 편의 메서드 //orderItem 과 Order 양쪽 동일하게 업데이트
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //주문 취소 OrderStatus - CANCELLED
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    //배송지 입력
    public void setDeliveryAddressForm(DeliveryAddressForm deliveryAddressForm) { //get 은 조회
        this.deliveryAddressForm = deliveryAddressForm;
    }

    //결제 방식
    public void selectPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    //총 결제 금액
    public int calculateLineTotalPrice() {
        return orderItems.stream()
                .filter(OrderItem::isSelected)
                .mapToInt(OrderItem::getLineTotal) // unitPrice * quantity
                .sum();
    }

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

    //최종 주문 스냅샷
    public Order createFinalOrder() {
        Order order = Order.builder() //파라미터가 많으면 가독성이 좋지 않음
                .member(member)
                .membershipGrade(member.getMembershipGrade())
                .orderStatus(OrderStatus.CREATED)
                .deliveryAddressForm(deliveryAddressForm)
                .paymentMethod(paymentMethod)
                .memberPoints(memberPoints)
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

