package com.janghankyu.kokoro.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "{signUpRequest.email.email}")
    @NotBlank(message = "{signUpRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{signUpRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "{signUpRequest.password.pattern}")
    private String password;

    @NotBlank(message = "{signUpRequest.username.notBlank}")
    @Size(min=2, message = "{signUpRequest.username.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.username.pattern}")
    private String username;

    @NotBlank(message = "{signUpRequest.nickname.notBlank}")
    @Size(min=2, message = "{signUpRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.nickname.pattern}")
    private String nickname;
}
