package com.xenocrm.simulator.enums;

/**
 * SimulationRunStatus -- Domain enum for status IN simulation_runs.
 * Layer: Domain Enum
 * Converter: SimulationRunStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum SimulationRunStatus {
    PENDING, RUNNING, COMPLETED, FAILED
}
