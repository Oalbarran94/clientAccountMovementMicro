package com.example.accounts.exceptions;

public class ClientWithAccountAlreadyExistingException extends RuntimeException{
    public ClientWithAccountAlreadyExistingException(String message) {
        super(message);
    }
}
