package com.xenocrm.channelservice.controller;

import com.xenocrm.channelservice.dto.ChannelCallbackPayloadDto;
import com.xenocrm.channelservice.service.CallbackProcessingService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ChannelCallbackController — Webhook endpoint for receiving callbacks from the channel stub.
 */
@RestController
@RequestMapping("/api/v1/callbacks/channel")
@RequiredArgsConstructor
@Tag(name = "Channel Callbacks", description = "Webhook endpoint for channel provider events")
public class ChannelCallbackController {

    private final CallbackProcessingService callbackProcessingService;

    @PostMapping
    @Operation(summary = "Receive channel callback webhook")
    public ResponseEntity<ResponseWrapper<Void>> receiveCallback(@Valid @RequestBody ChannelCallbackPayloadDto payloadDto) {
        callbackProcessingService.processCallback(payloadDto);
        return ResponseEntity.ok(ResponseWrapper.<Void>success(null, "Callback received and processed successfully"));
    }
}
