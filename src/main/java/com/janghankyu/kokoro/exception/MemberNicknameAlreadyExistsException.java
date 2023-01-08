package com.janghankyu.kokoro.exception;

public class MemberNicknameAlreadyExistsException extends RuntimeException {
    public MemberNicknameAlreadyExistsException(String message) {
        super(message);
    }
}
