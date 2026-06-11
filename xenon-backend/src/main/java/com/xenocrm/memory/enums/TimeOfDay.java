package com.xenocrm.memory.enums;

/**
 * TimeOfDay -- Domain enum for time_of_day IN org_memory_entries.
 * Layer: Domain Enum
 * Converter: TimeOfDayConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum TimeOfDay {
    MORNING, AFTERNOON, EVENING, NIGHT
}
