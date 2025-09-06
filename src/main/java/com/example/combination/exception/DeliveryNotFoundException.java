package com.example.combination.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long deliveryId) {
        super("등록되지 않은 배송정보입니다. id = " + deliveryId);
    }
}
