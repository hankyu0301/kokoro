package com.janghankyu.kokoro.service.sign;

import com.janghankyu.kokoro.config.token.TokenHelper;
import com.janghankyu.kokoro.dto.sign.RefreshTokenResponse;
import com.janghankyu.kokoro.dto.sign.SignInResponse;
import com.janghankyu.kokoro.dto.sign.SignUpRequest;
import com.janghankyu.kokoro.exception.LoginFailureException;
import com.janghankyu.kokoro.exception.MemberEmailAlreadyExistsException;
import com.janghankyu.kokoro.exception.MemberNicknameAlreadyExistsException;
import com.janghankyu.kokoro.exception.RefreshTokenFailureException;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.janghankyu.kokoro.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.janghankyu.kokoro.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static com.janghankyu.kokoro.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    @InjectMocks SignService signService;
    @Mock MemberRepository memberRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenHelper accessTokenHelper;
    @Mock TokenHelper refreshTokenHelper;

    @BeforeEach
        //동일한 타입의 @Mock에 대해 Mockito가 제대로 인식하지 못함.
    void beforeEach() {
        signService = new SignService(memberRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper);
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequest req = createSignUpRequest();

        // when
        signService.signUp(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validateSignUpByEmailTest() {
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void validateSignUpByDuplicateNicknameTest() {
        // given
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }


    @Test
    void signInTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(accessTokenHelper.createToken(any())).willReturn("access");
        given(refreshTokenHelper.createToken(any())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(createSignInRequest("email", "password"));

        // then
        assertThat(res.getAccessToken()).isEqualTo("access");
        assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(createSignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(createSignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void refreshTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.of(new TokenHelper.PrivateClaims("memberId", "ROLE_NORMAL")));
        given(accessTokenHelper.createToken(any())).willReturn(accessToken);

        // when
        RefreshTokenResponse res = signService.refreshToken(refreshToken);

        // then
        assertThat(res.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void refreshTokenExceptionByInvalidTokenTest() {
        // given
        String refreshToken = "refreshToken";
        given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(RefreshTokenFailureException.class);
    }
}
