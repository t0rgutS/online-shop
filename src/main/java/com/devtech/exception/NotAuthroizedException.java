package com.devtech.exception;

public class NotAuthroizedException extends RuntimeException {
    public NotAuthroizedException() {
        super("Вы не авторизированы!");
    }
}
