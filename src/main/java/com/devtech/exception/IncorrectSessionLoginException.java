package com.devtech.exception;

public class IncorrectSessionLoginException extends RuntimeException {
    public IncorrectSessionLoginException(String message) {
        super(message);
    }
}
