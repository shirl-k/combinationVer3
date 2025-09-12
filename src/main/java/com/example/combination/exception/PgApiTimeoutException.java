package com.example.combination.exception;

/**
 * PG사 API 타임아웃 예외
 * PG사 API 호출 시 타임아웃이 발생할 때 사용
 */
public class PgApiTimeoutException extends PgApiException {
    
    private final String apiName;
    private final int timeoutSeconds;
    
    public PgApiTimeoutException(String apiName, int timeoutSeconds) {
        super(String.format("PG사 API 타임아웃: %s, 타임아웃: %d초", apiName, timeoutSeconds));
        this.apiName = apiName;
        this.timeoutSeconds = timeoutSeconds;
    }
    
    public String getApiName() {
        return apiName;
    }
    
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }
}
