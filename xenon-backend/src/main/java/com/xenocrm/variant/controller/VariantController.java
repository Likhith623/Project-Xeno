package com.xenocrm.variant.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.variant.dto.MessageVariantCreateRequestDto;
import com.xenocrm.variant.dto.MessageVariantResponseDto;
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
    public ResponseEntity<ResponseWrapper<MessageVariantResponseDto>> createVariant(@Valid @RequestBody MessageVariantCreateRequestDto request) {
        MessageVariantResponseDto responseDto = variantService.createVariant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get all variants for a campaign")
    public ResponseEntity<ResponseWrapper<List<MessageVariantResponseDto>>> getVariantsByCampaign(@PathVariable UUID campaignId) {
        List<MessageVariantResponseDto> responseDtos = variantService.getVariantsByCampaignId(campaignId);
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get variant by ID")
    public ResponseEntity<ResponseWrapper<MessageVariantResponseDto>> getVariantById(@PathVariable UUID id) {
        MessageVariantResponseDto responseDto = variantService.getVariantById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update variant details")
    public ResponseEntity<ResponseWrapper<MessageVariantResponseDto>> updateVariant(
            @PathVariable UUID id,
            @Valid @RequestBody com.xenocrm.variant.dto.MessageVariantUpdateRequestDto request) {
        MessageVariantResponseDto responseDto = variantService.updateVariant(id, request);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete variant")
    public ResponseEntity<ResponseWrapper<Void>> deleteVariant(@PathVariable UUID id) {
        variantService.deleteVariant(id);
        return ResponseEntity.ok(ResponseWrapper.success(null));
    }
}
