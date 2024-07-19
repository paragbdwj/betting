package com.elephants.betting.src.exception;

public class PayoutNotFoundException extends RuntimeException{
    public PayoutNotFoundException(String message) {
        super(message);
    }
}
