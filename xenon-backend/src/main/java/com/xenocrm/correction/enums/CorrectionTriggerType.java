package com.xenocrm.correction.enums;

/**
 * CorrectionTriggerType -- Domain enum for trigger_type IN correction_events.
 * Layer: Domain Enum
 * Converter: CorrectionTriggerTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CorrectionTriggerType {
    HIGH_FAILURE_RATE, LOW_OPEN_RATE, LOW_CTR, CHANNEL_TIMEOUT, BOUNCE_SPIKE, OPT_OUT_SPIKE
}
