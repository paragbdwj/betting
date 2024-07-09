package com.elephants.betting.acquisition.exception;

public class PayoutNotFoundException extends RuntimeException{
    public PayoutNotFoundException(String message) {
        super(message);
    }
}
