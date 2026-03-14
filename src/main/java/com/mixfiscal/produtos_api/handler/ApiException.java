package com.mixfiscal.produtos_api.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String description;

    public ApiException(HttpStatus statusException, String message, String description, Exception e) {
        super(message, e);
        this.httpStatus = statusException;
        this.description = description;
    }
}
