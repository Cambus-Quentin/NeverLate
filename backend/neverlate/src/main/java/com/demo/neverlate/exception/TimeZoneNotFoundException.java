package com.demo.neverlate.exception;

public class TimeZoneNotFoundException extends RuntimeException {
    public TimeZoneNotFoundException(String message) {
        super(message);
    }
}