package com.xenocrm.segment.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.segment.dto.SegmentCreateRequestDto;
import com.xenocrm.segment.dto.SegmentResponseDto;
import com.xenocrm.segment.service.SegmentEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.xenocrm.common.PaginationMetadata;

/**
 * SegmentController — Exposes segment ingestion and evaluation endpoints.
 */
@RestController
@RequestMapping("/api/v1/segments")
@RequiredArgsConstructor
@Tag(name = "Segment", description = "Audience segmentation endpoints")
public class SegmentController {

    private final SegmentEvaluationService segmentEvaluationService;

    @PostMapping
    @Operation(summary = "Create a new segment")
    public ResponseEntity<ResponseWrapper<SegmentResponseDto>> createSegment(@Valid @RequestBody SegmentCreateRequestDto request) {
        SegmentResponseDto responseDto = segmentEvaluationService.createSegment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all segments with pagination")
    public ResponseEntity<ResponseWrapper<List<SegmentResponseDto>>> getAllSegments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SegmentResponseDto> pagedResult = segmentEvaluationService.getAllSegments(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get segment details")
    public ResponseEntity<ResponseWrapper<SegmentResponseDto>> getSegment(@PathVariable UUID id) {
        SegmentResponseDto responseDto = segmentEvaluationService.getSegmentById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @PostMapping("/{id}/evaluate")
    @Operation(summary = "Trigger async evaluation of a segment")
    public ResponseEntity<ResponseWrapper<Void>> evaluateSegment(@PathVariable UUID id) {
        segmentEvaluationService.evaluateSegmentAsync(id);
        return ResponseEntity.accepted().body(ResponseWrapper.success(null));
    }
}
