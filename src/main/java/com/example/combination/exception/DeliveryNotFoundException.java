package com.example.combination.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long id) {
        super("등록되지 않은 배송정보입니다. id = " + id);
    }
}
