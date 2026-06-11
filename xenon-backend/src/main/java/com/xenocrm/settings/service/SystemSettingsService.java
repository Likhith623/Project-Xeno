package com.xenocrm.settings.service;

import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.settings.dto.SystemSettingsDto;
import com.xenocrm.settings.entity.SystemSettingsEntity;
import com.xenocrm.settings.mapper.SystemSettingsMapper;
import com.xenocrm.settings.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SystemSettingsService — Handles system configuration CRUD operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingsService {

    private final SystemSettingsRepository systemSettingsRepository;
    private final SystemSettingsMapper systemSettingsMapper;

    @Transactional
    public SystemSettingsDto createOrUpdateSetting(SystemSettingsDto request) {
        log.debug("Creating or updating setting with key: {}", request.getKey());

        SystemSettingsEntity entity = systemSettingsRepository.findByKey(request.getKey())
                .orElseGet(SystemSettingsEntity::new);

        entity.setKey(request.getKey());
        entity.setValue(request.getValue());
        
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }

        SystemSettingsEntity savedEntity = systemSettingsRepository.save(entity);
        return systemSettingsMapper.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public SystemSettingsDto getSettingByKey(String key) {
        SystemSettingsEntity entity = systemSettingsRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("SystemSettings", "key", key));
        return systemSettingsMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<SystemSettingsDto> getAllSettings() {
        return systemSettingsRepository.findAll().stream()
                .map(systemSettingsMapper::toDto)
                .collect(Collectors.toList());
    }
}
