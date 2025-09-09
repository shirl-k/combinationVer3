package com.example.combination.domain.tracking;

/**
 * 배송 추적 상태 열거형
 * 배송 과정의 각 단계를 나타냅니다.
 */
public enum TrackingStatus {
    PREPARING("상품 준비 중"),           // 상품 포장 및 준비
    PICKED_UP("상품 픽업 완료"),         // 택배사 픽업 완료
    IN_TRANSIT("배송 중"),              // 배송 중
    OUT_FOR_DELIVERY("배송 출발"),       // 배송 출발
    DELIVERED("배송 완료"),             // 배송 완료
    DELIVERY_FAILED("배송 실패"),        // 배송 실패
    RETURNED("반송"),                   // 반송
    CANCELLED("배송 취소");             // 배송 취소
    
    private final String description;
    
    TrackingStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 다음 단계로 진행 가능한지 확인
     */
    public boolean canProgressTo(TrackingStatus nextStatus) {
        return switch (this) {
            case PREPARING -> nextStatus == PICKED_UP || nextStatus == CANCELLED;
            case PICKED_UP -> nextStatus == IN_TRANSIT || nextStatus == RETURNED;
            case IN_TRANSIT -> nextStatus == OUT_FOR_DELIVERY || nextStatus == RETURNED;
            case OUT_FOR_DELIVERY -> nextStatus == DELIVERED || nextStatus == DELIVERY_FAILED;
            case DELIVERED, DELIVERY_FAILED, RETURNED, CANCELLED -> false;
        };
    }
}
