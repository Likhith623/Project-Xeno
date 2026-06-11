package com.xenocrm.exception;

/**
 * XenoCrmException — Base exception for all custom CRM exceptions.
 * Layer: Exception
 * Purpose: Provides a common superclass for custom application exceptions.
 */
public class XenoCrmException extends RuntimeException {
    
    public XenoCrmException(String message) {
        super(message);
    }
    
    public XenoCrmException(String message, Throwable cause) {
        super(message, cause);
    }
}
