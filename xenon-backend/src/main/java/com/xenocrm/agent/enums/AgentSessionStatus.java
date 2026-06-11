package com.xenocrm.agent.enums;

/**
 * AgentSessionStatus -- Domain enum for status IN agent_sessions.
 * Layer: Domain Enum
 * Converter: AgentSessionStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum AgentSessionStatus {
    RUNNING, COMPLETED, FAILED, PAUSED
}
