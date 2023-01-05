package com.janghankyu.kokoro.config.security;

import com.janghankyu.kokoro.config.token.TokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TokenHelper accessTokenHelper;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return accessTokenHelper.parse(token)
                .map(this::convert)
                .orElse(null);
    }

    private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.getMemberId(),
                Stream.of(privateClaims.getMemberRole()).map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
