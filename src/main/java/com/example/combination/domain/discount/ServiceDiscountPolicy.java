package com.example.combination.domain.discount;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;
import org.springframework.stereotype.Component;

/**
 * 서비스별 할인 정책
 * - 배송서비스: 무료배송
 * - 이사서비스: 추가 할인 혜택
 */
@Component
public class ServiceDiscountPolicy implements DiscountPolicy {
    
    private static final int FREE_DELIVERY_THRESHOLD = 50000; // 5만원 이상 무료배송
    private static final int MOVING_SERVICE_DISCOUNT_RATE = 5; // 이사서비스 5% 추가 할인
    
    @Override
    public int calculateDiscount(int originalPrice, Member member, ServiceType serviceType) {
        if (!isApplicable(member, serviceType)) {
            return 0;
        }
        
        return switch (serviceType) {
            case JUST_DELIVERY -> calculateDeliveryDiscount(originalPrice);
            case MOVING_SERVICE -> calculateMovingServiceDiscount(originalPrice);
        };
    }
    
    private int calculateDeliveryDiscount(int originalPrice) {
        // 5만원 이상 구매 시 배송비 무료 (배송비는 이미 0원이므로 추가 할인 없음)
        return originalPrice >= FREE_DELIVERY_THRESHOLD ? 0 : 0;
    }
    
    private int calculateMovingServiceDiscount(int originalPrice) {
        // 이사서비스 이용 시 5% 추가 할인
        return (originalPrice * MOVING_SERVICE_DISCOUNT_RATE) / 100;
    }
    
    @Override
    public boolean isApplicable(Member member, ServiceType serviceType) {
        return member != null && serviceType != null;
    }
    
    @Override
    public int getPriority() {
        return 2; // 멤버십 할인 다음 우선순위
    }
}
