package com.example.combination.dto;

import com.example.combination.domain.payment.PaymentMethod;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgPaymentRequestDTO {
    
    private Long orderId;
    private String orderName; // 주문명
    private int amount; // 결제 금액
    private PaymentMethod paymentMethod;
    private String customerName; // 고객명
    private String customerEmail; // 고객 이메일
    private String customerPhone; // 고객 전화번호
    private String successUrl; // 결제 성공 시 리다이렉트 URL
    private String failUrl; // 결제 실패 시 리다이렉트 URL
    private String cancelUrl; // 결제 취소 시 리다이렉트 URL
    private String extraData; // 추가 데이터 (JSON 형태)
}
