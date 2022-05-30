package com.luminn.firebase.twilio;

public class PhoneVerificationException extends RuntimeException{
    public PhoneVerificationException(String message) {
        super(message);
    }
}
