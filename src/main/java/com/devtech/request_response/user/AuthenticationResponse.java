package com.devtech.request_response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    @NotNull(message = "Укажите логин!")
    @NotEmpty(message = "Укажите логин!")
    private String login;

    @NotNull(message = "Укажите JWT-токен!")
    @NotEmpty(message = "Укажите JWT-токен!")
    private String token;
}
