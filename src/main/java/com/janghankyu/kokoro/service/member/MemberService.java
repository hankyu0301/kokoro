package com.janghankyu.kokoro.service.member;

import com.janghankyu.kokoro.dto.member.MemberDto;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.exception.MemberNotFoundException;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDto read(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

    @PreAuthorize("")
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }
}
