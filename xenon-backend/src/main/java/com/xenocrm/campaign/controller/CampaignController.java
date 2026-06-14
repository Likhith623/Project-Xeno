package com.xenocrm.campaign.controller;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignPerformanceDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.dto.CampaignStatusUpdateRequestDto;
import com.xenocrm.campaign.dto.MabStatsDto;
import com.xenocrm.campaign.dto.OptOutAlertDto;
import com.xenocrm.campaign.service.CampaignExecutionService;
import com.xenocrm.campaign.service.CampaignService;
import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.correction.dto.CorrectionEventResponseDto;
import com.xenocrm.correction.service.CorrectionRetrievalService;
import com.xenocrm.variant.service.VariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private final VariantService variantService;
    private final CorrectionRetrievalService correctionRetrievalService;
    private final com.xenocrm.simulator.service.AudienceSimulationOrchestrationService orchestrationService;
    private final com.xenocrm.simulator.mapper.SimulationMapper simulationMapper;
    private final com.xenocrm.campaign.service.TimelineStorytellingService timelineService;
    private final com.xenocrm.campaign.service.CampaignAnalyticsService analyticsService;

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

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update campaign status (pause, cancel, etc.)")
    public ResponseEntity<ResponseWrapper<CampaignResponseDto>> updateCampaignStatus(
            @PathVariable UUID id,
            @Valid @RequestBody CampaignStatusUpdateRequestDto request) {
        CampaignResponseDto responseDto = campaignService.updateCampaignStatus(id, request.getStatus().name());
        return ResponseEntity.ok(ResponseWrapper.success(responseDto, "Campaign status updated successfully"));
    }

    @GetMapping("/{id}/performance")
    @Operation(summary = "Get campaign performance metrics including delivery, open, and conversion rates")
    public ResponseEntity<ResponseWrapper<CampaignPerformanceDto>> getCampaignPerformance(@PathVariable UUID id) {
        CampaignPerformanceDto performanceDto = campaignService.getCampaignPerformance(id);
        return ResponseEntity.ok(ResponseWrapper.success(performanceDto));
    }

    @GetMapping("/opt-out-alerts")
    @Operation(summary = "Get opt-out alerts for running campaigns exceeding safety thresholds")
    public ResponseEntity<ResponseWrapper<List<OptOutAlertDto>>> getOptOutAlerts() {
        List<OptOutAlertDto> alerts = campaignService.getOptOutAlerts();
        return ResponseEntity.ok(ResponseWrapper.success(alerts));
    }

    @GetMapping("/{id}/variants/mab-stats")
    @Operation(summary = "Get Thompson Sampling (MAB) statistics for campaign variants")
    public ResponseEntity<ResponseWrapper<List<MabStatsDto>>> getMabStats(@PathVariable UUID id) {
        List<MabStatsDto> stats = variantService.getMabStats(id);
        return ResponseEntity.ok(ResponseWrapper.success(stats));
    }

    @GetMapping("/{id}/corrections")
    @Operation(summary = "Get self-correction events for a specific campaign")
    public ResponseEntity<ResponseWrapper<List<CorrectionEventResponseDto>>> getCampaignCorrections(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CorrectionEventResponseDto> pagedResult = correctionRetrievalService.getCorrectionsByCampaignId(id, pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
    }

    @PostMapping("/{id}/simulate")
    @Operation(summary = "Trigger a simulation for a specific campaign")
    public ResponseEntity<ResponseWrapper<com.xenocrm.simulator.dto.SimulationRunResultDto>> triggerSimulationForCampaign(
            @PathVariable UUID id, 
            @RequestBody com.xenocrm.simulator.dto.SimulationRunRequestDto requestDto) {
        requestDto.setCampaignId(id);
        com.xenocrm.simulator.entity.SimulationRunEntity run = orchestrationService.triggerSimulation(requestDto);
        return ResponseEntity.ok(ResponseWrapper.success(simulationMapper.toResultDto(run), "Simulation started successfully"));
    }

    @GetMapping("/{id}/analytics/narrative")
    @Operation(summary = "Get a natural language narrative analysis of the campaign's performance")
    public ResponseEntity<ResponseWrapper<List<String>>> getCampaignNarrativeAnalytics(
            @PathVariable UUID id) {
        List<String> analysis = analyticsService.getNaturalLanguageAnalytics(id);
        return ResponseEntity.ok(ResponseWrapper.success(analysis, "Generated narrative analytics"));
    }

    @GetMapping("/{id}/timeline")
    @Operation(summary = "Get a chronological narrative timeline of the campaign")
    public ResponseEntity<ResponseWrapper<List<String>>> getCampaignTimeline(
            @PathVariable UUID id) {
        List<String> timeline = timelineService.getCampaignTimeline(id);
        return ResponseEntity.ok(ResponseWrapper.success(timeline, "Generated campaign timeline"));
    }

    @GetMapping("/proposals")
    @Operation(summary = "Get all autonomous AI campaign proposals awaiting human approval (Tinder Swipe UI)")
    public ResponseEntity<ResponseWrapper<List<CampaignResponseDto>>> getCampaignProposals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Let's use CampaignService.getAllCampaigns and filter
        List<CampaignResponseDto> drafts = campaignService.getAllCampaigns(PageRequest.of(0, 100))
                .getContent().stream()
                .filter(c -> "DRAFT".equals(c.getStatus()))
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(ResponseWrapper.success(drafts, "Retrieved AI proposals for review"));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve an AI proposed campaign and execute it")
    public ResponseEntity<ResponseWrapper<CampaignResponseDto>> approveCampaign(@PathVariable UUID id) {
        CampaignResponseDto responseDto = campaignService.updateCampaignStatus(id, "APPROVED");
        campaignExecutionService.executeCampaignAsync(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto, "Campaign approved and execution started"));
    }
}
