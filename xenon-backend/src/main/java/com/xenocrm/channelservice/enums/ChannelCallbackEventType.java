package com.xenocrm.channelservice.enums;

/**
 * ChannelCallbackEventType -- Domain enum for event_type IN channel_callbacks.
 * Layer: Domain Enum
 * Converter: ChannelCallbackEventTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum ChannelCallbackEventType {
    DELIVERED, FAILED, OPENED, READ, CLICKED, CONVERTED, UNSUBSCRIBED, BOUNCED, EXPIRED
}
