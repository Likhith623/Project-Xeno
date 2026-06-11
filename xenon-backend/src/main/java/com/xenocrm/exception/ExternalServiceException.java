package com.xenocrm.exception;

/**
 * ExternalServiceException — Thrown when an external service call fails.
 * Layer: Exception
 * Purpose: Represents failures from downstream services like Gemini or Channel Service.
 */
public class ExternalServiceException extends XenoCrmException {
    
    private final String serviceName;
    
    public ExternalServiceException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
}
