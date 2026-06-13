package com.xenocrm.segment.controller;

import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.segment.dto.SegmentCreateRequestDto;
import com.xenocrm.segment.dto.SegmentMemberResponseDto;
import com.xenocrm.segment.dto.SegmentResponseDto;
import com.xenocrm.segment.entity.SegmentMemberEntity;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import com.xenocrm.segment.repository.SegmentMemberRepository;
import com.xenocrm.segment.service.SegmentEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * SegmentController — Exposes segment ingestion and evaluation endpoints.
 */
@RestController
@RequestMapping("/api/v1/segments")
@RequiredArgsConstructor
@Tag(name = "Segment", description = "Audience segmentation endpoints")
public class SegmentController {

    private final SegmentEvaluationService segmentEvaluationService;
    private final SegmentMemberRepository segmentMemberRepository;
    private final AudienceSegmentRepository audienceSegmentRepository;

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

    @PatchMapping("/{id}")
    @Operation(summary = "Update segment details")
    public ResponseEntity<ResponseWrapper<SegmentResponseDto>> updateSegment(
            @PathVariable UUID id,
            @Valid @RequestBody com.xenocrm.segment.dto.SegmentUpdateRequestDto request) {
        SegmentResponseDto responseDto = segmentEvaluationService.updateSegment(id, request);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Get paginated list of customer IDs in a segment")
    public ResponseEntity<ResponseWrapper<List<SegmentMemberResponseDto>>> getSegmentMembers(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (!audienceSegmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("AudienceSegment", "id", id);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SegmentMemberEntity> membersPage = segmentMemberRepository.findAllBySegmentId(id, pageable);
        List<SegmentMemberResponseDto> dtos = membersPage.getContent().stream()
                .map(m -> SegmentMemberResponseDto.builder()
                        .segmentId(m.getId().getSegmentId())
                        .customerId(m.getId().getCustomerId())
                        .addedAt(m.getAddedAt())
                        .build())
                .collect(Collectors.toList());
        Page<SegmentMemberResponseDto> resultPage = new PageImpl<>(dtos, pageable, membersPage.getTotalElements());
        return ResponseEntity.ok(ResponseWrapper.success(dtos, PaginationMetadata.from(resultPage)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a segment")
    public ResponseEntity<ResponseWrapper<Void>> deleteSegment(@PathVariable UUID id) {
        segmentEvaluationService.deleteSegment(id);
        return ResponseEntity.ok(ResponseWrapper.success(null, "Segment deleted successfully"));
    }

    @PostMapping("/{id}/evaluate")
    @Operation(summary = "Trigger async evaluation of a segment")
    public ResponseEntity<ResponseWrapper<Void>> evaluateSegment(@PathVariable UUID id) {
        segmentEvaluationService.evaluateSegmentAsync(id);
        return ResponseEntity.accepted().body(ResponseWrapper.success(null));
    }

    @GetMapping("/{id}/persona")
    @Operation(summary = "Generate an AI persona for this segment")
    public ResponseEntity<ResponseWrapper<java.util.Map<String, Object>>> getSegmentPersona(
            @PathVariable UUID id,
            @org.springframework.beans.factory.annotation.Autowired com.xenocrm.segment.service.PersonaGenerationService personaGenerationService) {
        java.util.Map<String, Object> persona = personaGenerationService.generatePersona(id);
        return ResponseEntity.ok(ResponseWrapper.success(persona, "Persona generated successfully"));
    }
}
