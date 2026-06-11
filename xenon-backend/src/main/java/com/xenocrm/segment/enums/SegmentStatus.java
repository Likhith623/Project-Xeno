package com.xenocrm.segment.enums;

/**
 * SegmentStatus -- Domain enum for status IN (draft,building,ready,archived).
 * Layer: Domain Enum
 * Converter: SegmentStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum SegmentStatus {
    DRAFT, BUILDING, READY, ARCHIVED
}
