package com.example.combination.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long cartId) {
        super("장바구니를 찾을 수 없습니다. id=" + cartId);
    }
}
