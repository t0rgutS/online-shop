package com.devtech.request_response.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class AuthenticationRequest {
    @NotNull(message = "Укажите логин!")
    @NotEmpty(message = "Укажите логин!")
    private String login;

    @NotNull(message = "Укажите пароль!")
    @NotEmpty(message = "Укажите пароль!")
    private String password;
}
