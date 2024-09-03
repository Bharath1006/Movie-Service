package com.bharath.firstspringproject.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException (String message)
    {
        super(message);
    }
}
