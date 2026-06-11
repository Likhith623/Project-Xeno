package com.xenocrm.exception;

/**
 * ResourceNotFoundException — Thrown when a requested resource is not found.
 * Layer: Exception
 * Purpose: Represents HTTP 404 Not Found condition.
 */
public class ResourceNotFoundException extends XenoCrmException {
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
