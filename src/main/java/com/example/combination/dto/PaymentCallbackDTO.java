package com.example.combination.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallbackDTO {
    
    private String transactionId;
    private String approvalNumber;
    private String status; // success, fail, cancel
    private int amount;
    private String paymentMethod;
    private String signature; // PG사 서명 검증용
    private String rawData; // 원본 콜백 데이터
}
