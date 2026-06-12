package com.xenocrm.memory.controller;

import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.memory.dto.OrgMemoryEntryDto;
import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import com.xenocrm.memory.mapper.OrgMemoryMapper;
import com.xenocrm.memory.repository.OrgMemoryEntryRepository;
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
 * MemoryController — API endpoints for viewing organizational memory.
 */
@RestController
@RequestMapping("/api/v1/memory")
@RequiredArgsConstructor
@Tag(name = "Organizational Memory", description = "Endpoints for AI knowledge base")
public class MemoryController {

    private final com.xenocrm.memory.service.MemoryRetrievalService memoryService;

    @GetMapping
    @Operation(summary = "Get all organizational memory entries")
    public ResponseEntity<ResponseWrapper<Page<OrgMemoryEntryDto>>> getAllMemories(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<OrgMemoryEntryDto> memoriesDto = memoryService.getAllMemories(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                memoriesDto,
                "Retrieved organizational memory",
                PaginationMetadata.from(memoriesDto)
        ));
    }

    @GetMapping("/query")
    @Operation(summary = "Query organizational memory by segment tag and channel")
    public ResponseEntity<ResponseWrapper<Page<OrgMemoryEntryDto>>> queryMemories(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String segmentTag,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String channel,
            @PageableDefault(size = 20) Pageable pageable) {
        
        com.xenocrm.channelservice.enums.MessageChannel messageChannel = null;
        if (channel != null) {
            try {
                messageChannel = com.xenocrm.channelservice.enums.MessageChannel.valueOf(channel.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore invalid channel
            }
        }
        
        Page<OrgMemoryEntryDto> memoriesDto = memoryService.getMemoriesByQuery(segmentTag, messageChannel, pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                memoriesDto,
                "Queried organizational memory",
                PaginationMetadata.from(memoriesDto)
        ));
    }
}
