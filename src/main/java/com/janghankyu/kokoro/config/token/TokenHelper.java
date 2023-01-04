package com.janghankyu.kokoro.config.token;


import com.janghankyu.kokoro.handler.JwtHandler;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenHelper {

    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    private static final String MEMBER_ROLE = "MEMBER_ROLE";
    private static final String MEMBER_ID = "MEMBER_ID";

    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.getMemberId(), MEMBER_ROLE, privateClaims.getMemberRole()),
                maxAgeSeconds
        );
    }

    public Optional<PrivateClaims> parse(String token) {
        return jwtHandler.parse(key, token).map(this::convert);
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                claims.get(MEMBER_ROLE, String.class)
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String memberId;
        private String memberRole;
    }
}

