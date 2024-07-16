package com.library.backend.exception;

public class OperationNotPermittedException extends RuntimeException {

    public OperationNotPermittedException(String s) {
        super(s);
    }
}
