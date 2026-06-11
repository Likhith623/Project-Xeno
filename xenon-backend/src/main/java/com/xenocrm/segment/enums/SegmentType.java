package com.xenocrm.segment.enums;

/**
 * SegmentType -- Domain enum for type IN (STATIC,DYNAMIC,AI_GENERATED).
 * Layer: Domain Enum
 * Converter: SegmentTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum SegmentType {
    STATIC, DYNAMIC, AI_GENERATED
}
