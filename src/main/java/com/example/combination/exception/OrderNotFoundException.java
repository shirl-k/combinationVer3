package com.example.combination.exception;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(Long orderId) {
    super("주문 없음 orderId = " + orderId);
  }
}
