package com.abhijeet.airbnb_backend.exception;

public class MethodArgumentNotValidException extends RuntimeException{
    public MethodArgumentNotValidException(String message){
        super(message);
    }
}
