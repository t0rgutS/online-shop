package com.devtech.exception;

public class TooYoungUserException extends RuntimeException {
    public TooYoungUserException() {
        super("Сожалеем, но вы малой. Идите нахуй.");
    }
}
