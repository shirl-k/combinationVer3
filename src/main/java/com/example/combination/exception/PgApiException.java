package com.example.combination.exception;

public class PgApiException extends RuntimeException {
    
    private final String errorCode;
    
    public PgApiException(String message) {
        super(message);
        this.errorCode = null;
    }
    
    public PgApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public PgApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }
    
    public PgApiException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
