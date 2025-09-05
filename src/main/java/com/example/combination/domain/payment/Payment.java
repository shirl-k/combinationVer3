package com.example.combination.domain.payment;

import com.example.combination.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)         //결제 세부 단계 추적
    private PaymentStatus paymentStatus; //PENDING(결제 시도 중(PG사 요청 단계), AUTHORIZED(결제 승인(카드 승인, 포인트 차감 등)), PAID(최종 결제 완료)
                                            // ,FAILED(결제 실패: 잔액 부족, 네트워크 오류 등),CANCELLED(결제 취소: 사용자 요청, PG사 취소),REFUNDED(환불 완료)
                                            //주문 1건에 여러 payment있을 수 있으니, 주문과 별도의 엔티티 두는게 베스트
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private LocalDateTime paidDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

}
