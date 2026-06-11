package com.xenocrm.memory.enums;

/**
 * MemoryLearningType -- Domain enum for learning_type IN org_memory_entries.
 * Layer: Domain Enum
 * Converter: MemoryLearningTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum MemoryLearningType {
    COPY_STYLE, SEND_TIME, CHANNEL_PREFERENCE, FREQUENCY, OFFER_TYPE, SUBJECT_PATTERN
}
