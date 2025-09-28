package com.example.customers.exceptions;


public class ClientCreationException extends RuntimeException {
    public ClientCreationException(String message) {
        super(message);
    }
}
