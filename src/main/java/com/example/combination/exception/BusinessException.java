package com.example.combination.exception;

/**
 * 비즈니스 로직 관련 예외의 최상위 클래스
 * 모든 비즈니스 예외는 이 클래스를 상속받아 구현
 */
public abstract class BusinessException extends RuntimeException {
    
    /**
     * 기본 생성자
     * @param message 예외 메시지
     */
    protected BusinessException(String message) {
        super(message);
    }
    
    /**
     * 원인 예외를 포함하는 생성자
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
