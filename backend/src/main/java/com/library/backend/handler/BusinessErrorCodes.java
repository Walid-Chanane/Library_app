package com.library.backend.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {

    //use whatever you like as error codes
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(303, HttpStatus.BAD_REQUEST, "Incorrect password"),
    NEW_PASSWORD_DOES_NOT_MATCH(304, HttpStatus.BAD_REQUEST, "Password does not match"),
    ACCOUNT_LOCKED(555, HttpStatus.LOCKED, "Account locked"),
    ACCOUNT_DISABLED(556, HttpStatus.FORBIDDEN, "Account disabled"),
    BAD_CREDENTIALS(550, HttpStatus.FORBIDDEN, "Incorrect email and / or password!")

    ;

    @Getter
    private final int code;

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
