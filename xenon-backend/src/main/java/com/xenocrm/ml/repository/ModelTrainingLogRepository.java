package com.xenocrm.ml.repository;

import com.xenocrm.ml.entity.ModelTrainingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ModelTrainingLogRepository — Spring Data JPA repository for ML logs.
 */
@Repository
public interface ModelTrainingLogRepository extends JpaRepository<ModelTrainingLogEntity, UUID> {
    List<ModelTrainingLogEntity> findByModelNameOrderByCreatedAtDesc(String modelName);
}
