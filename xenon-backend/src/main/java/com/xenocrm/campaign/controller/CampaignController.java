package com.xenocrm.campaign.controller;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.service.CampaignExecutionService;
import com.xenocrm.campaign.service.CampaignService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CampaignController — Exposes campaign ingestion and execution endpoints.
 */
@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
@Tag(name = "Campaign", description = "Campaign management and execution endpoints")
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignExecutionService campaignExecutionService;

    @PostMapping
    @Operation(summary = "Create a new campaign")
    public ResponseEntity<ResponseWrapper<CampaignResponseDto>> createCampaign(@Valid @RequestBody CampaignCreateRequestDto request) {
        CampaignResponseDto responseDto = campaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get campaign details and metrics")
    public ResponseEntity<ResponseWrapper<CampaignResponseDto>> getCampaign(@PathVariable UUID id) {
        CampaignResponseDto responseDto = campaignService.getCampaignById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @PostMapping("/{id}/execute")
    @Operation(summary = "Trigger async execution of a campaign")
    public ResponseEntity<ResponseWrapper<Void>> executeCampaign(@PathVariable UUID id) {
        campaignExecutionService.executeCampaignAsync(id);
        return ResponseEntity.accepted().body(ResponseWrapper.success(null));
    }
}
