package com.TeamOne411.backend.service;

public class UsernameExistsException extends Throwable {
    public UsernameExistsException(String message) {
        super(message);
    }
}
