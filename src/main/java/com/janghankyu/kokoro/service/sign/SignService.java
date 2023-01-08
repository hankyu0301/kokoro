package com.janghankyu.kokoro.service.sign;


import com.janghankyu.kokoro.config.token.TokenHelper;
import com.janghankyu.kokoro.dto.sign.RefreshTokenResponse;
import com.janghankyu.kokoro.dto.sign.SignInRequest;
import com.janghankyu.kokoro.dto.sign.SignInResponse;
import com.janghankyu.kokoro.dto.sign.SignUpRequest;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.entity.member.MemberRole;
import com.janghankyu.kokoro.exception.LoginFailureException;
import com.janghankyu.kokoro.exception.MemberEmailAlreadyExistsException;
import com.janghankyu.kokoro.exception.MemberNicknameAlreadyExistsException;
import com.janghankyu.kokoro.exception.RefreshTokenFailureException;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        String encodedPassword = passwordEncoder.encode(req.getPassword());
        memberRepository.save(
                new Member(req.getEmail(), encodedPassword, req.getUsername(), req.getNickname(), MemberRole.ROLE_NORMAL)
        );
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        String refreshToken = refreshTokenHelper.createToken(privateClaims);
        return new SignInResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshToken(String rToken) {
        TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(rToken).orElseThrow(RefreshTokenFailureException::new);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        return new RefreshTokenResponse(accessToken);
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private TokenHelper.PrivateClaims createPrivateClaims(Member member) {
        return new TokenHelper.PrivateClaims(
                String.valueOf(member.getId()),
                String.valueOf(member.getMemberRole()));
    }
}
