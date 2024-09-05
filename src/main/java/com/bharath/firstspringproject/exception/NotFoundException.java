package com.bharath.firstspringproject.exception;


public class NotFoundException extends RuntimeException {

    public NotFoundException(String message){
        super(message);
    }
}