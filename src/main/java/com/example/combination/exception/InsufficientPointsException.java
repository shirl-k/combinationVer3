package com.example.combination.exception;

/**
 * 포인트 부족 예외
 * 사용자가 보유한 포인트보다 많은 포인트를 사용하려고 할 때 발생
 */
public class InsufficientPointsException extends BusinessException {
    
    private final int requestedPoints;
    private final int availablePoints;
    
    public InsufficientPointsException(int requestedPoints, int availablePoints) {
        super(String.format("포인트 부족: 요청=%d, 보유=%d", requestedPoints, availablePoints));
        this.requestedPoints = requestedPoints;
        this.availablePoints = availablePoints;
    }
    
    public int getRequestedPoints() {
        return requestedPoints;
    }
    
    public int getAvailablePoints() {
        return availablePoints;
    }
}
