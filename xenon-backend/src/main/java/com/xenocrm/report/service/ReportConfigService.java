package com.xenocrm.report.service;

import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.report.dto.ReportConfigDto;
import com.xenocrm.report.entity.ReportConfigEntity;
import com.xenocrm.report.mapper.ReportConfigMapper;
import com.xenocrm.report.repository.ReportConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ReportConfigService — Handles CRUD for report configurations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportConfigService {

    private final ReportConfigRepository reportConfigRepository;
    private final ReportConfigMapper reportConfigMapper;

    @Transactional
    public ReportConfigDto createReportConfig(ReportConfigDto request) {
        log.debug("Creating new report config: {}", request.getName());

        ReportConfigEntity entity = reportConfigMapper.toEntity(request);
        
        if (request.getIsActive() != null) {
            entity.setActive(request.getIsActive());
        } else {
            entity.setActive(true);
        }

        ReportConfigEntity savedEntity = reportConfigRepository.save(entity);
        return reportConfigMapper.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public ReportConfigDto getReportConfigById(UUID id) {
        ReportConfigEntity entity = reportConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReportConfig", "id", id));
        return reportConfigMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ReportConfigDto> getAllReportConfigs() {
        return reportConfigRepository.findAll().stream()
                .map(reportConfigMapper::toDto)
                .collect(Collectors.toList());
    }
}
