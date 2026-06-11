package com.xenocrm.channelservice.enums;

/**
 * CallbackProcessingStatus -- Domain enum for processing_status IN channel_callbacks.
 * Layer: Domain Enum
 * Converter: CallbackProcessingStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CallbackProcessingStatus {
    PENDING, PROCESSED, ERROR
}
