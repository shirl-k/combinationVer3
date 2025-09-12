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
@Table(name = "payments")
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
    
    @Column(nullable = false)
    private LocalDateTime paidDate;
    
    @Column
    private LocalDateTime cancelledDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(nullable = false)
    private int amount; // 결제 금액
    
    @Column(length = 100)
    private String pgTransactionId; // PG사 거래 ID
    
    @Column(length = 100)
    private String pgApprovalNumber; // PG사 승인번호
    
    @Column(length = 500)
    private String pgResponseData; // PG사 응답 데이터 (JSON)
    
    @Column(length = 200)
    private String failureReason; // 실패 사유
    
    @Column(length = 100)
    private String refundTransactionId; // 환불 거래 ID
    
    @Column
    private LocalDateTime refundDate; // 환불 일시
    
    // 결제 상태 업데이트
    public void updateStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
        if (newStatus == PaymentStatus.PAID) {
            this.paidDate = LocalDateTime.now();
        } else if (newStatus == PaymentStatus.CANCELLED) {
            this.cancelledDate = LocalDateTime.now();
        } else if (newStatus == PaymentStatus.REFUNDED) {
            this.refundDate = LocalDateTime.now();
        }
    }
    
    // PG사 응답 데이터 저장
    public void setPgResponse(String transactionId, String approvalNumber, String responseData) {
        this.pgTransactionId = transactionId;
        this.pgApprovalNumber = approvalNumber;
        this.pgResponseData = responseData;
    }
    
    // 실패 사유 설정
    public void setFailureReason(String reason) {
        this.failureReason = reason;
    }
    
    // 환불 거래 ID 설정
    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }
    
    // 결제 생성
    public static Payment createPayment(Order order, PaymentMethod method, int amount) {
        return Payment.builder()
                .order(order)
                .paymentMethod(method)
                .amount(amount)
                .paymentStatus(PaymentStatus.PENDING)
                .paidDate(LocalDateTime.now())
                .build();
    }

}
