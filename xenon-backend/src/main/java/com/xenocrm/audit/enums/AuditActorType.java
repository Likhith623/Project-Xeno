package com.xenocrm.audit.enums;

/**
 * AuditActorType -- Domain enum for actor_type IN audit_logs.
 * Layer: Domain Enum
 * Converter: AuditActorTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum AuditActorType {
    USER, AGENT, SYSTEM
}
