package com.example.combination.domain.discount;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;

/**
 * 할인 정책 인터페이스
 * 다양한 할인 정책을 구현할 수 있도록 확장 가능한 설계
 */
public interface DiscountPolicy {
    
    /**
     * 할인 금액을 계산합니다.
     * @param originalPrice 원가
     * @param member 회원 정보
     * @param serviceType 서비스 타입
     * @return 할인 금액
     */
    int calculateDiscount(int originalPrice, Member member, ServiceType serviceType);
    
    /**
     * 해당 정책이 적용 가능한지 확인합니다.
     * @param member 회원 정보
     * @param serviceType 서비스 타입
     * @return 적용 가능 여부
     */
    boolean isApplicable(Member member, ServiceType serviceType);
    
    /**
     * 정책 우선순위를 반환합니다. (낮을수록 높은 우선순위)
     * @return 우선순위
     */
    int getPriority();
}
