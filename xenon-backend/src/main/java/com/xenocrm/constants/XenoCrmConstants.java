package com.xenocrm.constants;

/**
 * XenoCrmConstants — Application-wide constants to avoid magic strings and numbers.
 * Layer: Constants
 * Purpose: Centralized location for constant values.
 */
public final class XenoCrmConstants {

    private XenoCrmConstants() {
        // Prevent instantiation
    }

    public static final String DEFAULT_CURRENCY = "INR";
    public static final String DEFAULT_COUNTRY = "IN";
    public static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
    
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "ERROR";
    
    // Add more constants as needed
}
