package com.devtech.exception;

public class AddingYourOwnProductException extends RuntimeException {
    public AddingYourOwnProductException() {
        super("Вы не можете добавить в корзину свой же товар!");
    }
}
