package com.janghankyu.kokoro.entity.member;

import com.janghankyu.kokoro.entity.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public Member(String email, String password, String username, String nickname, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}