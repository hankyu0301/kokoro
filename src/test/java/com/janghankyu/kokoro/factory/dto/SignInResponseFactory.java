package com.janghankyu.kokoro.factory.dto;

import com.janghankyu.kokoro.dto.sign.SignInResponse;

public class SignInResponseFactory {
    public static SignInResponse createSignInResponse(String accessToken, String refreshToken) {
        return new SignInResponse(accessToken, refreshToken);
    }
}
