package com.example.combination.dto;

import com.example.combination.domain.payment.PaymentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    
    private Long paymentId;
    private Long orderId;
    private PaymentStatus paymentStatus;
    private String pgTransactionId;
    private String pgApprovalNumber;
    private int amount;
    private String message;
    private String redirectUrl; // PG사 결제 페이지 URL (카드 결제 시)
    private String failureReason;
}
