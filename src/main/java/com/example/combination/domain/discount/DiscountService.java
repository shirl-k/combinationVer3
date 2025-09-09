package com.example.combination.domain.discount;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 할인 계산 서비스
 * 여러 할인 정책을 조합하여 최적의 할인을 계산합니다.
 */
@Service
@RequiredArgsConstructor
public class DiscountService {
    
    private final List<DiscountPolicy> discountPolicies;
    
    /**
     * 총 할인 금액을 계산합니다.
     * @param originalPrice 원가
     * @param member 회원 정보
     * @param serviceType 서비스 타입
     * @return 총 할인 금액
     */
    public int calculateTotalDiscount(int originalPrice, Member member, ServiceType serviceType) {
        return discountPolicies.stream()
                .filter(policy -> policy.isApplicable(member, serviceType))
                .sorted(Comparator.comparingInt(DiscountPolicy::getPriority))
                .mapToInt(policy -> policy.calculateDiscount(originalPrice, member, serviceType))
                .sum();
    }
    
    /**
     * 최종 결제 금액을 계산합니다.
     * @param originalPrice 원가
     * @param member 회원 정보
     * @param serviceType 서비스 타입
     * @return 최종 결제 금액
     */
    public int calculateFinalPrice(int originalPrice, Member member, ServiceType serviceType) {
        int totalDiscount = calculateTotalDiscount(originalPrice, member, serviceType);
        return Math.max(0, originalPrice - totalDiscount);
    }
    
    /**
     * 할인 내역을 상세히 반환합니다.
     * @param originalPrice 원가
     * @param member 회원 정보
     * @param serviceType 서비스 타입
     * @return 할인 내역
     */
    public DiscountResult getDiscountDetails(int originalPrice, Member member, ServiceType serviceType) {
        DiscountResult result = new DiscountResult();
        result.setOriginalPrice(originalPrice);
        
        discountPolicies.stream()
                .filter(policy -> policy.isApplicable(member, serviceType))
                .sorted(Comparator.comparingInt(DiscountPolicy::getPriority))
                .forEach(policy -> {
                    int discount = policy.calculateDiscount(originalPrice, member, serviceType);
                    result.addDiscountDetail(policy.getClass().getSimpleName(), discount);
                });
        
        result.setFinalPrice(originalPrice - result.getTotalDiscount());
        return result;
    }
    
    /**
     * 할인 결과를 담는 클래스
     */
    public static class DiscountResult {
        private int originalPrice;
        private int totalDiscount;
        private int finalPrice;
        private java.util.Map<String, Integer> discountDetails = new java.util.LinkedHashMap<>();
        
        // Getters and Setters
        public int getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(int originalPrice) { this.originalPrice = originalPrice; }
        
        public int getTotalDiscount() { return totalDiscount; }
        public void setTotalDiscount(int totalDiscount) { this.totalDiscount = totalDiscount; }
        
        public int getFinalPrice() { return finalPrice; }
        public void setFinalPrice(int finalPrice) { this.finalPrice = finalPrice; }
        
        public java.util.Map<String, Integer> getDiscountDetails() { return discountDetails; }
        
        public void addDiscountDetail(String policyName, int discount) {
            if (discount > 0) {
                discountDetails.put(policyName, discount);
                totalDiscount += discount;
            }
        }
    }
}
