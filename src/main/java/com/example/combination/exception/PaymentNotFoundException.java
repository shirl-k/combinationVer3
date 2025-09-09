package com.example.combination.exception;

public class PaymentNotFoundException extends RuntimeException {
    
    public PaymentNotFoundException(Long paymentId) {
        super("결제 정보를 찾을 수 없습니다. Payment ID: " + paymentId);
    }
    
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
