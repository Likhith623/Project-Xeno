package com.xenocrm.correction.enums;

/**
 * CorrectionActionType -- Domain enum for action_taken IN correction_events.
 * Layer: Domain Enum
 * Converter: CorrectionActionTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CorrectionActionType {
    SWITCH_CHANNEL, REWRITE_COPY, PAUSE_CAMPAIGN, REDUCE_FREQUENCY, ADD_FALLBACK, NO_ACTION
}
