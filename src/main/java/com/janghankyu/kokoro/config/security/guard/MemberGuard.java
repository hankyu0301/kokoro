package com.janghankyu.kokoro.config.security.guard;

import com.janghankyu.kokoro.entity.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {
    private List<MemberRole> roleTypes = List.of(MemberRole.ROLE_ADMIN);

    @Override
    protected List<MemberRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthHelper.extractMemberId());
    }
}
