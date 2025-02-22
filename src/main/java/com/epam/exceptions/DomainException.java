package com.epam.exceptions;

public class DomainException extends RuntimeException{
    public DomainException(String message) {
        super(message);
    }
}
