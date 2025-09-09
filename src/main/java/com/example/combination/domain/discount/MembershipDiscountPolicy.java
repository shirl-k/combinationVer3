package com.example.combination.domain.discount;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import org.springframework.stereotype.Component;

/**
 * 멤버십 등급별 할인 정책
 */
@Component
public class MembershipDiscountPolicy implements DiscountPolicy {
    
    @Override
    public int calculateDiscount(int originalPrice, Member member, ServiceType serviceType) {
        if (!isApplicable(member, serviceType)) {
            return 0;
        }
        
        MembershipGrade grade = member.getMembershipGrade();
        if (grade == null) {
            return 0;
        }
        
        // 멤버십 등급별 할인율
        double discountRate = switch (grade) {
            case DIAMOND -> 0.15; // 15%
            case GOLD -> 0.10;    // 10%
            case SILVER -> 0.05;  // 5%
            case CRYSTAL -> 0.03; // 3%
            case PEARL -> 0.01;   // 1%
        };
        
        return (int) (originalPrice * discountRate);
    }
    
    @Override
    public boolean isApplicable(Member member, ServiceType serviceType) {
        return member != null && 
               member.getMembershipGrade() != null && 
               member.getMemberStatus() != null &&
               member.getMemberStatus().name().equals("ACTIVE");
    }
    
    @Override
    public int getPriority() {
        return 1; // 높은 우선순위
    }
}
