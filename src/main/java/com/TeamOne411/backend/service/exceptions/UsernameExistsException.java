package com.TeamOne411.backend.service.exceptions;

public class UsernameExistsException extends Throwable {
    public UsernameExistsException(String message) {
        super(message);
    }
}
