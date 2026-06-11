package com.xenocrm.memory.controller;

import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.memory.dto.OrgMemoryEntryDto;
import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import com.xenocrm.memory.mapper.OrgMemoryMapper;
import com.xenocrm.memory.repository.OrgMemoryRepository;
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

    private final OrgMemoryRepository memoryRepository;
    private final OrgMemoryMapper memoryMapper;

    @GetMapping
    @Operation(summary = "Get all organizational memory entries")
    public ResponseEntity<ResponseWrapper<Page<OrgMemoryEntryDto>>> getAllMemories(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<OrgMemoryEntryEntity> memories = memoryRepository.findAll(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                memories.map(memoryMapper::toDto),
                "Retrieved organizational memory",
                PaginationMetadata.from(memories)
        ));
    }
}
