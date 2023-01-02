package com.janghankyu.kokoro.repository.member;

import com.janghankyu.kokoro.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
