package com.example.combination.exception;

/**
 * 배송 주소 예외
 * 배송 불가 지역이거나 잘못된 주소 형식일 때 발생
 */
public class InvalidDeliveryAddressException extends BusinessException {
    
    private final String address;
    private final String reason;
    
    public InvalidDeliveryAddressException(String address, String reason) {
        super(String.format("배송 불가 주소: %s, 사유: %s", address, reason));
        this.address = address;
        this.reason = reason;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getReason() {
        return reason;
    }
}
