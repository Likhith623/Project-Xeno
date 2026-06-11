package com.xenocrm.communication.enums;

/**
 * CommunicationStatus -- Domain enum for status IN communications.
 * Layer: Domain Enum
 * Converter: CommunicationStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CommunicationStatus {
    PENDING, SENT, DELIVERED, FAILED, OPENED, READ, CLICKED, CONVERTED, UNSUBSCRIBED, BOUNCED, EXPIRED
}
