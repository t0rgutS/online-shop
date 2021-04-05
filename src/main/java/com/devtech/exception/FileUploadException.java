package com.devtech.exception;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super("Ошибка загрузки файла: " + message);
    }
}
