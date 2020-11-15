package com.TeamOne411.backend.service.exceptions;

public class EmailExistsException extends Exception {
    public EmailExistsException(String message) {
        super(message);
    }
}
