package com.example.combination.domain.member;

public enum MembershipGrade {
    DIAMOND,GOLD,SILVER,CRYSTAL,PEARL

    public static MembershipGrade calculateGrade(int totalSpent) {
        if(totalSpent<=1000000)

    }
}


public MembershipGrade grading(Member member) {
    if( >= 0 && accumulatedPayment <= 1000000) {
        return MembershipGrade.PEARL;
    }else if(accumulatedPayment >1000000 && accumulatedPayment <= 3000000) {
        return MembershipGrade.SILVER;
    }else if(accumulatedPayment >3000000 && accumulatedPayment <= 7000000) {
        return MembershipGrade.GOLD;
    }else if(accumulatedPayment >7000000 && accumulatedPayment <= 10000000) {
        return MembershipGrade.DIAMOND;
    }else return MembershipGrade.VIP;

}
