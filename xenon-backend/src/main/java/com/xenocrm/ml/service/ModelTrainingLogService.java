package com.xenocrm.ml.service;

import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.ml.dto.ModelTrainingLogDto;
import com.xenocrm.ml.entity.ModelTrainingLogEntity;
import com.xenocrm.ml.mapper.ModelTrainingLogMapper;
import com.xenocrm.ml.repository.ModelTrainingLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ModelTrainingLogService — Handles creation and retrieval of ML training logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModelTrainingLogService {

    private final ModelTrainingLogRepository modelTrainingLogRepository;
    private final ModelTrainingLogMapper modelTrainingLogMapper;

    @Transactional
    public ModelTrainingLogDto logTraining(ModelTrainingLogDto request) {
        log.debug("Logging new model training: {}", request.getModelName());

        ModelTrainingLogEntity entity = modelTrainingLogMapper.toEntity(request);
        
        if (entity.getStatus() == null) {
            entity.setStatus("STARTED");
        }
        if (entity.getStartedAt() == null) {
            entity.setStartedAt(OffsetDateTime.now());
        }

        ModelTrainingLogEntity savedEntity = modelTrainingLogRepository.save(entity);
        return modelTrainingLogMapper.toDto(savedEntity);
    }

    @Transactional
    public ModelTrainingLogDto completeTraining(UUID id, String status, java.util.Map<String, Object> metrics) {
        log.debug("Completing model training: {}", id);

        ModelTrainingLogEntity entity = modelTrainingLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ModelTrainingLog", "id", id));

        entity.setStatus(status);
        if (metrics != null) {
            entity.setMetrics(metrics);
        }
        entity.setCompletedAt(OffsetDateTime.now());

        ModelTrainingLogEntity updatedEntity = modelTrainingLogRepository.save(entity);
        return modelTrainingLogMapper.toDto(updatedEntity);
    }

    @Transactional(readOnly = true)
    public List<ModelTrainingLogDto> getLogsByModelName(String modelName) {
        return modelTrainingLogRepository.findByModelNameOrderByCreatedAtDesc(modelName).stream()
                .map(modelTrainingLogMapper::toDto)
                .collect(Collectors.toList());
    }
}
