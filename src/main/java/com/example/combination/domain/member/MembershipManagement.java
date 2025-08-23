package com.example.combination.domain.member;

import com.example.combination.domain.business.MembershipPolicy;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "membershipManagement")
public class MembershipManagement implements MembershipPolicy {

    @Id
    @GeneratedValue
    private Long id;
/*
    @Column
    private Pearl pearl;
    private Silver silver;
    private Gold gold;
    private DIAMOND diamond;
    private VIP vip;

*/

    private int accumulatedPayment;


    public MembershipGrade grading(Member member) {
        if(accumulatedPayment >= 0 && accumulatedPayment <= 1000000) {
            return MembershipGrade.PEARL;
        }else if(accumulatedPayment >1000000 && accumulatedPayment <= 3000000) {
            return MembershipGrade.SILVER;
        }else if(accumulatedPayment >3000000 && accumulatedPayment <= 7000000) {
            return MembershipGrade.GOLD;
        }else if(accumulatedPayment >7000000 && accumulatedPayment <= 10000000) {
            return MembershipGrade.DIAMOND;
        }else return MembershipGrade.VIP;

    }


    @Override
    public void birthdayEvent() {

    }

    @Override
    public void upgradeEvent() {

    }
}
