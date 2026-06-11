package com.xenocrm.correction.enums;

/**
 * CorrectionOutcome -- Domain enum for correction_outcome IN correction_events.
 * Layer: Domain Enum
 * Converter: CorrectionOutcomeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CorrectionOutcome {
    IMPROVED, NEUTRAL, WORSENED, INSUFFICIENT_DATA
}
