package com.xenocrm.variant.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.variant.dto.VariantCreateRequestDto;
import com.xenocrm.variant.dto.VariantResponseDto;
import com.xenocrm.variant.service.VariantService;
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
 * VariantController — Exposes campaign variant endpoints.
 */
@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
@Tag(name = "Variant", description = "Campaign Variant endpoints")
public class VariantController {

    private final VariantService variantService;

    @PostMapping
    @Operation(summary = "Create a new variant for a campaign")
    public ResponseEntity<ResponseWrapper<VariantResponseDto>> createVariant(@Valid @RequestBody VariantCreateRequestDto request) {
        VariantResponseDto responseDto = variantService.createVariant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get all variants for a campaign")
    public ResponseEntity<ResponseWrapper<List<VariantResponseDto>>> getVariantsByCampaign(@PathVariable UUID campaignId) {
        List<VariantResponseDto> responseDtos = variantService.getVariantsByCampaignId(campaignId);
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
