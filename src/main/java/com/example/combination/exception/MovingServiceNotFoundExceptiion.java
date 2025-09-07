package com.example.combination.exception;

public class MovingServiceNotFoundExceptiion extends RuntimeException {
    public MovingServiceNotFoundExceptiion(Long id) {
        super("등록되지 않은 배송 정보입니다. id = " + id);
    }
}
