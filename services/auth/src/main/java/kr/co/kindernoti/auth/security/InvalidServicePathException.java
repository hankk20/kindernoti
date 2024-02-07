package kr.co.kindernoti.auth.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidServicePathException extends ResponseStatusException {

    public InvalidServicePathException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
