package ru.avaliev.training.springconsoleapp.exception;

public class DuplicateRecordException extends Exception {

    public DuplicateRecordException(String message) {
        super(message);
    }
}
