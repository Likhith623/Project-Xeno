package com.xenocrm.agent.enums;

/**
 * AgentDecisionType -- Domain enum for decision_type IN agent_decisions.
 * Layer: Domain Enum
 * Converter: AgentDecisionTypeConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum AgentDecisionType {
    SEGMENT_QUERY, VARIANT_GENERATION, CHANNEL_SELECTION, SCHEDULE_DECISION, SEND_COMMAND, ABORT, MEMORY_LOOKUP, SIMULATION_TRIGGER, CORRECTION_TRIGGER
}
