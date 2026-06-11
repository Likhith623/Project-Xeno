package com.xenocrm.campaign.enums;

/**
 * CampaignStatus -- Domain enum for status IN campaigns.
 * Layer: Domain Enum
 * Converter: CampaignStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CampaignStatus {
    DRAFT, SIMULATING, SCHEDULED, RUNNING, PAUSED, COMPLETED, CANCELLED, FAILED
}
