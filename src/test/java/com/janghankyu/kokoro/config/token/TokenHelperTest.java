package com.janghankyu.kokoro.config.token;

import com.janghankyu.kokoro.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TokenHelperTest {
    TokenHelper tokenHelper;

    @BeforeEach
    void beforeEach() {
        tokenHelper = new TokenHelper(new JwtHandler(),"myKey", 1000L);
    }

    @Test
    void createTokenAndParseTest() {
        // given
        String memberId = "1";
        String memberRole = "ROLE_NORMAL";
        TokenHelper.PrivateClaims privateClaims = new TokenHelper.PrivateClaims(memberId, memberRole);

        // when
        String token = tokenHelper.createToken(privateClaims);

        // then
        TokenHelper.PrivateClaims parsedPrivateClaims = tokenHelper.parse(token).orElseThrow(RuntimeException::new);
        assertThat(parsedPrivateClaims.getMemberId()).isEqualTo(memberId);
        assertThat(parsedPrivateClaims.getMemberRole()).isEqualTo(memberRole);
    }
}