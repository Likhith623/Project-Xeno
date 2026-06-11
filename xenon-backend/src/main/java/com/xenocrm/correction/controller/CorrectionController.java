package com.xenocrm.correction.controller;

import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.correction.dto.CorrectionEventResponseDto;
import com.xenocrm.correction.entity.CorrectionEventEntity;
import com.xenocrm.correction.mapper.CorrectionEventMapper;
import com.xenocrm.correction.repository.CorrectionEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CorrectionController — API endpoints for viewing AI self-correction events.
 */
@RestController
@RequestMapping("/api/v1/corrections")
@RequiredArgsConstructor
@Tag(name = "Self-Correction Engine", description = "Endpoints for AI campaign self-corrections")
public class CorrectionController {

    private final com.xenocrm.correction.service.CorrectionRetrievalService correctionRetrievalService;

    @GetMapping
    @Operation(summary = "Get all correction events")
    public ResponseEntity<ResponseWrapper<Page<CorrectionEventResponseDto>>> getCorrections(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<CorrectionEventResponseDto> dtos = correctionRetrievalService.getCorrections(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                dtos,
                "Retrieved correction events",
                PaginationMetadata.from(dtos)
        ));
    }
}
