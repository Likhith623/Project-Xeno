package com.xenocrm.report.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.report.dto.ReportConfigDto;
import com.xenocrm.report.service.ReportConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * ReportConfigController — Exposes report configuration endpoints.
 */
@RestController
@RequestMapping("/api/v1/reports/configs")
@RequiredArgsConstructor
@Tag(name = "Report", description = "Report configuration endpoints")
public class ReportConfigController {

    private final ReportConfigService reportConfigService;

    @PostMapping
    @Operation(summary = "Create a new report config")
    public ResponseEntity<ResponseWrapper<ReportConfigDto>> createReportConfig(@Valid @RequestBody ReportConfigDto request) {
        ReportConfigDto responseDto = reportConfigService.createReportConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a report config by id")
    public ResponseEntity<ResponseWrapper<ReportConfigDto>> getReportConfig(@PathVariable UUID id) {
        ReportConfigDto responseDto = reportConfigService.getReportConfigById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all report configs")
    public ResponseEntity<ResponseWrapper<List<ReportConfigDto>>> getAllReportConfigs() {
        List<ReportConfigDto> responseDtos = reportConfigService.getAllReportConfigs();
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
