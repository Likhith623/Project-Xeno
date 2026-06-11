package com.xenocrm.communication.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.communication.dto.CommunicationResponseDto;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.communication.service.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CommunicationController — Exposes communication endpoints.
 */
@RestController
@RequestMapping("/api/v1/communications")
@RequiredArgsConstructor
@Tag(name = "Communication", description = "Communication endpoints")
public class CommunicationController {

    private final CommunicationService communicationService;

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get communications for a campaign")
    public ResponseEntity<ResponseWrapper<List<CommunicationResponseDto>>> getByCampaign(@PathVariable UUID campaignId) {
        List<CommunicationResponseDto> communications = communicationService.getCommunicationsByCampaignId(campaignId);
        return ResponseEntity.ok(ResponseWrapper.success(communications));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get communications for a customer")
    public ResponseEntity<ResponseWrapper<List<CommunicationResponseDto>>> getByCustomer(@PathVariable UUID customerId) {
        List<CommunicationResponseDto> communications = communicationService.getCommunicationsByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(communications));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update status of a communication")
    public ResponseEntity<ResponseWrapper<Void>> updateStatus(@PathVariable UUID id, @RequestParam CommunicationStatus status) {
        communicationService.updateStatus(id, status);
        return ResponseEntity.ok(ResponseWrapper.success(null));
    }
}
