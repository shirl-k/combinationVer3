package com.example.combination.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String userId) {
        super("회원이 존재하지 않습니다. id=" + userId);
    }
}
