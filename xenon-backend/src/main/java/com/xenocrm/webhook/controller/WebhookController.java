package com.xenocrm.webhook.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.webhook.dto.WebhookConfigDto;
import com.xenocrm.webhook.dto.WebhookLogDto;
import com.xenocrm.webhook.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * WebhookController — Exposes webhook endpoints.
 */
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhook", description = "Webhook configuration and execution endpoints")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/configs")
    @Operation(summary = "Create a new webhook configuration")
    public ResponseEntity<ResponseWrapper<WebhookConfigDto>> createConfig(@Valid @RequestBody WebhookConfigDto request) {
        WebhookConfigDto responseDto = webhookService.createConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/configs")
    @Operation(summary = "Get all webhook configurations")
    public ResponseEntity<ResponseWrapper<List<WebhookConfigDto>>> getAllConfigs() {
        List<WebhookConfigDto> responseDtos = webhookService.getAllConfigs();
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }

    @PostMapping("/trigger/{eventType}")
    @Operation(summary = "Manually trigger webhooks for an event type")
    public ResponseEntity<ResponseWrapper<Void>> triggerWebhooks(@PathVariable String eventType, @RequestBody Map<String, Object> payload) {
        webhookService.triggerWebhooksAsync(eventType, payload);
        return ResponseEntity.accepted().body(ResponseWrapper.success(null));
    }

    @GetMapping("/{webhookId}/logs")
    @Operation(summary = "Get execution logs for a specific webhook")
    public ResponseEntity<ResponseWrapper<List<WebhookLogDto>>> getLogsByWebhookId(@PathVariable UUID webhookId) {
        List<WebhookLogDto> logs = webhookService.getLogsByWebhookId(webhookId);
        return ResponseEntity.ok(ResponseWrapper.success(logs));
    }
}
