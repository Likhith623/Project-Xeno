package com.xenocrm.campaign.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CampaignUpdateRequestDto {
    @Schema(description = "Name of the campaign", example = "Summer Sale Retargeting")
    private String name;

    @Schema(description = "Description of the campaign")
    private String description;

    @Schema(description = "Natural language goal for the campaign", example = "Maximize conversions for summer items")
    private String goal;

    @Schema(description = "Target segment ID")
    private UUID targetSegmentId;
}
