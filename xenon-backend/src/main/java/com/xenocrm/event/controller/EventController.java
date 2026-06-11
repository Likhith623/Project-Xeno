package com.xenocrm.event.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.event.dto.EventCreateRequestDto;
import com.xenocrm.event.dto.EventResponseDto;
import com.xenocrm.event.service.EventService;
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
 * EventController — Exposes event ingestion endpoints.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Event", description = "Customer activity event endpoints")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Ingest a new event")
    public ResponseEntity<ResponseWrapper<EventResponseDto>> createEvent(@Valid @RequestBody EventCreateRequestDto request) {
        EventResponseDto responseDto = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all events for a customer")
    public ResponseEntity<ResponseWrapper<List<EventResponseDto>>> getEventsByCustomer(@PathVariable UUID customerId) {
        List<EventResponseDto> responseDtos = eventService.getEventsByCustomerId(customerId);
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
