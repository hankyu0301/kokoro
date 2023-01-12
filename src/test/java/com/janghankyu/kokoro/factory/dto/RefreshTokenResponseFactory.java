package com.janghankyu.kokoro.factory.dto;

import com.janghankyu.kokoro.dto.sign.RefreshTokenResponse;

public class RefreshTokenResponseFactory {
    public static RefreshTokenResponse createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponse(accessToken);
    }
}
