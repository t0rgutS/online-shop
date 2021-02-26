package com.devtech.exception;

public class NoProductsLeftException extends RuntimeException {
    public NoProductsLeftException() {
        super("Данный товар закончился!");
    }
}
