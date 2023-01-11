package com.janghankyu.kokoro.config.security.guard;

import com.janghankyu.kokoro.config.security.CustomUserDetails;
import com.janghankyu.kokoro.entity.member.MemberRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHelper {

    public static Long extractMemberId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public static Set<MemberRole> extractMemberRoles() {
        return getUserDetails().getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .map(strAuth -> MemberRole.valueOf(strAuth))
                .collect(Collectors.toSet());
    }

    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
