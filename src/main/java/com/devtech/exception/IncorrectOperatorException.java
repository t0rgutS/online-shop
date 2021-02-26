package com.devtech.exception;

public class IncorrectOperatorException extends Exception {
    public IncorrectOperatorException(String incorrect) {
        super("Неизвестный оператор: '" + incorrect + "'!");
    }
}
