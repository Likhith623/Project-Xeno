package com.xenocrm.segment.service;

import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.segment.dto.SegmentCreateRequestDto;
import com.xenocrm.segment.dto.SegmentResponseDto;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentStatus;
import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.mapper.SegmentMapper;
import com.xenocrm.segment.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * SegmentEvaluationService — Handles segment creation and evaluation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentEvaluationService {

    private final SegmentRepository segmentRepository;
    private final SegmentMapper segmentMapper;

    @Transactional
    public SegmentResponseDto createSegment(SegmentCreateRequestDto request) {
        log.debug("Creating new segment: {}", request.getName());

        AudienceSegmentEntity segment = segmentMapper.toEntity(request);
        
        if (segment.getType() == null) {
            segment.setType(SegmentType.DYNAMIC);
        }
        if (segment.getStatus() == null) {
            segment.setStatus(SegmentStatus.DRAFT);
        }

        AudienceSegmentEntity savedSegment = segmentRepository.save(segment);
        return segmentMapper.toResponseDto(savedSegment);
    }

    @Transactional(readOnly = true)
    public SegmentResponseDto getSegmentById(UUID id) {
        AudienceSegmentEntity segment = segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AudienceSegment", "id", id));
        return segmentMapper.toResponseDto(segment);
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> evaluateSegmentAsync(UUID id) {
        log.debug("Evaluating segment async: {}", id);

        return CompletableFuture.supplyAsync(() -> {
            AudienceSegmentEntity segment = segmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("AudienceSegment", "id", id));

            int simulatedCount = (int) (Math.random() * 1000);
            
            segment.setCustomerCount(simulatedCount);
            segment.setLastEvaluatedAt(OffsetDateTime.now());
            
            segmentRepository.save(segment);
            log.debug("Successfully evaluated segment {}. Count: {}", id, simulatedCount);
            return null;
        });
    }
}
