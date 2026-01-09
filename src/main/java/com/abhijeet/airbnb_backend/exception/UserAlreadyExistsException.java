package com.abhijeet.airbnb_backend.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String messages){
        super(messages);
    }
}
