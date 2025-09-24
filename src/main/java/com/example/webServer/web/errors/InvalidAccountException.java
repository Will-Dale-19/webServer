package com.example.webServer.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException() {
        super();
    }

    public InvalidAccountException(String message) {
        super(message);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccountException(Throwable cause) {
        super(cause);
    }
}
