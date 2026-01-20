
package com.finance.tracker.exception;

/**
 * Exception thrown when requested resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
