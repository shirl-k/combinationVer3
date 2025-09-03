package com.example.combination.domain.member;

public enum MembershipGrade {
    DIAMOND,GOLD,SILVER,CRYSTAL,PEARL;

    public static MembershipGrade calculateGrade(int totalSpent) {
        if(totalSpent>=1000000) {
            return CRYSTAL;
        }else if(totalSpent>=300000) {
            return SILVER;
        }else if(totalSpent>=700000) {
            return GOLD;
        }else if(totalSpent>=10000000) {
            return DIAMOND;
        }else return PEARL;
        }
    }

