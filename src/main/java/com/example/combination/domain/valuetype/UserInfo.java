package com.example.combination.domain.valuetype;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "userInfo")
public class UserInfo {
    private String birth;
    private String gender;
    private String password;

    @Column(unique = true)
    private String nickname;
    private String phoneNum;

    @Column(unique = true, nullable = false)
    private String email;

    private String marketingInfo;
}
