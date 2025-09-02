package com.example.combination.domain.member;

import com.example.combination.domain.business.MembershipPolicy;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@Entity
@Table(name = "membershipManagement")
public class MembershipManagement implements MembershipPolicy {

    @Id
    @GeneratedValue
    private Long id;

/*  등급 조정
    @Column
    private Pearl pearl;
    private Silver silver;
    private Gold gold;
    private DIAMOND diamond;
    private VIP vip;

*/

//    private AccumulatedPayment accumulatedPayment;



    @Override
    public void birthdayEvent() {

    }

    @Override
    public void upgradeEvent() {

    }
}
