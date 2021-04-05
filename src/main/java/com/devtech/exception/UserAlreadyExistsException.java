package com.devtech.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String login) {
        super("Пользователь " + login + " уже зарегистрирован!");
    }
}
