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
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.xenocrm.common.PaginationMetadata;

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
    @Operation(summary = "Create a new campaign manually")
    public ResponseEntity<ResponseWrapper<CampaignResponseDto>> createCampaign(@Valid @RequestBody CampaignCreateRequestDto request) {
        CampaignResponseDto responseDto = campaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all campaigns with pagination")
    public ResponseEntity<ResponseWrapper<List<CampaignResponseDto>>> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CampaignResponseDto> pagedResult = campaignService.getAllCampaigns(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
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
