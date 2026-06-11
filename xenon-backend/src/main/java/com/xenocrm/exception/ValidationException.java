package com.xenocrm.exception;

/**
 * ValidationException — Thrown when input validation fails.
 * Layer: Exception
 * Purpose: Represents HTTP 400 Bad Request condition for business rules.
 */
public class ValidationException extends XenoCrmException {
    
    public ValidationException(String message) {
        super(message);
    }
}
