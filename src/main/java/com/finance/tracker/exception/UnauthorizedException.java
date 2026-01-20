package com.finance.tracker.exception;

/**
 * Exception thrown when user is not authorized to access resource
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}