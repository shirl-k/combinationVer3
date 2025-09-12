package com.example.combination.exception;

import java.time.LocalDate;

/**
 * 이사 날짜 예외
 * 과거 날짜이거나 휴일 등 이사 불가 날짜일 때 발생
 */
public class InvalidMovingDateException extends BusinessException {
    
    private final LocalDate movingDate;
    private final String reason;
    
    public InvalidMovingDateException(LocalDate movingDate, String reason) {
        super(String.format("이사 불가 날짜: %s, 사유: %s", movingDate, reason));
        this.movingDate = movingDate;
        this.reason = reason;
    }
    
    public LocalDate getMovingDate() {
        return movingDate;
    }
    
    public String getReason() {
        return reason;
    }
}
