package com.example.accounts.exceptions;

public class ClientNotActiveException extends RuntimeException{
    public ClientNotActiveException(String message) {
        super(message);
    }
}
