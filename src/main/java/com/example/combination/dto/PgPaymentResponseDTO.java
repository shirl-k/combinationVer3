package com.example.combination.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgPaymentResponseDTO {
    
    private boolean success;
    private String transactionId;
    private String approvalNumber;
    private String message;
    private String redirectUrl; // PG사 결제 페이지 URL
    private String rawResponse; // PG사 원본 응답 데이터
    private String errorCode;
    private String errorMessage;
}
