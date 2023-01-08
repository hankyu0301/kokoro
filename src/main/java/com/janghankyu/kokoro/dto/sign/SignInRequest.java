package com.janghankyu.kokoro.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Email(message = "{signInRequest.email.email}")
    @NotBlank(message = "{signInRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{signInRequest.password.notBlank}")
    private String password;
}