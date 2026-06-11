package com.xenocrm.settings.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.settings.dto.SystemSettingsDto;
import com.xenocrm.settings.service.SystemSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SystemSettingsController — Exposes system configuration endpoints.
 */
@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "System configuration endpoints")
public class SystemSettingsController {

    private final SystemSettingsService systemSettingsService;

    @PostMapping
    @Operation(summary = "Create or update a system setting")
    public ResponseEntity<ResponseWrapper<SystemSettingsDto>> createOrUpdateSetting(@Valid @RequestBody SystemSettingsDto request) {
        SystemSettingsDto responseDto = systemSettingsService.createOrUpdateSetting(request);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get a system setting by key")
    public ResponseEntity<ResponseWrapper<SystemSettingsDto>> getSettingByKey(@PathVariable String key) {
        SystemSettingsDto responseDto = systemSettingsService.getSettingByKey(key);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all system settings")
    public ResponseEntity<ResponseWrapper<List<SystemSettingsDto>>> getAllSettings() {
        List<SystemSettingsDto> responseDtos = systemSettingsService.getAllSettings();
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
