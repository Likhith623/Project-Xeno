package com.xenocrm.communication.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.communication.dto.CommunicationLogResponseDto;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.communication.service.CommunicationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * CommunicationLogController — Exposes communication log endpoints.
 */
@RestController
@RequestMapping("/api/v1/communications")
@RequiredArgsConstructor
@Tag(name = "Communication", description = "Communication logs endpoints")
public class CommunicationLogController {

    private final CommunicationLogService communicationLogService;

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get communication logs for a campaign")
    public ResponseEntity<ResponseWrapper<List<CommunicationLogResponseDto>>> getLogsByCampaign(@PathVariable UUID campaignId) {
        List<CommunicationLogResponseDto> logs = communicationLogService.getLogsByCampaign(campaignId);
        return ResponseEntity.ok(ResponseWrapper.success(logs));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get communication logs for a customer")
    public ResponseEntity<ResponseWrapper<List<CommunicationLogResponseDto>>> getLogsByCustomer(@PathVariable UUID customerId) {
        List<CommunicationLogResponseDto> logs = communicationLogService.getLogsByCustomer(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(logs));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update status of a communication log")
    public ResponseEntity<ResponseWrapper<Void>> updateStatus(@PathVariable UUID id, @RequestParam CommunicationStatus status) {
        communicationLogService.updateStatus(id, status);
        return ResponseEntity.ok(ResponseWrapper.success(null));
    }
}
