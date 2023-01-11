package com.janghankyu.kokoro.config.security.guard;


import com.janghankyu.kokoro.entity.member.MemberRole;

import java.util.List;

public abstract class Guard {
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<MemberRole> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<MemberRole> roleTypes) {
        return roleTypes.stream().allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
}
