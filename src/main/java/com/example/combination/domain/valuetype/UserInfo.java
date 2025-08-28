package com.example.combination.domain.valuetype;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "userInfo")
public class UserInfo {
    private String birth;
    private String gender;
     //@Valid
    private String password; //@Valid
    private String nickname;
    private String phoneNum;
    private String email;
    private String marketingInfo;
}
