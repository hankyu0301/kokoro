package com.janghankyu.kokoro.config.security.guard;

import com.janghankyu.kokoro.entity.member.MemberRole;
import com.janghankyu.kokoro.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostGuard extends Guard {
    private final PostRepository postRepository;
    private List<MemberRole> roleTypes = List.of(MemberRole.ROLE_ADMIN);

    @Override
    protected List<MemberRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return postRepository.findById(id)
                .map(post -> post.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(AuthHelper.extractMemberId()))
                .isPresent();
    }
}