package com.TeamOne411.backend.service;

public class EmailExistsException extends Exception {
    public EmailExistsException(String message) {
        super(message);
    }
}
