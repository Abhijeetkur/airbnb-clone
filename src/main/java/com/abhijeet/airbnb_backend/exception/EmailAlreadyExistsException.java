package com.abhijeet.airbnb_backend.exception;

public class EmailAlreadyExistsException  extends RuntimeException {
    public EmailAlreadyExistsException(String email){
        super("The email address '" + email + "' is already registered.");
    }
}
