package com.janghankyu.kokoro.factory.entity;

import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.entity.member.MemberRole;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFactory {

    public static Member createMember() {
        return new Member("email@email.com", "123456a!", "username", "nickname", MemberRole.ROLE_NORMAL);
    }

    public static Member createMemberWithId(Long id) {
        Member member = new Member("email@email.com", "123456a!", "username", "nickname", MemberRole.ROLE_NORMAL);
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createMember(String email, String password, String username, String nickname) {
        return new Member(email, password, username, nickname, MemberRole.ROLE_NORMAL);
    }


}
