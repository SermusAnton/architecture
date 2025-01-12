package org.example.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthenticateException extends RuntimeException {
    public AuthenticateException(String message) {
        super(message);
    }
}