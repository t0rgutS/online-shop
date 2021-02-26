package com.devtech.exception;

public class NoContactsException extends RuntimeException {
    public NoContactsException() {
        super("Сначала укажите контакты продавца!");
    }
}
