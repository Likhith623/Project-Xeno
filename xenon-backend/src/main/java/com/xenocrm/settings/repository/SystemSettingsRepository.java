package com.xenocrm.settings.repository;

import com.xenocrm.settings.entity.SystemSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * SystemSettingsRepository — Spring Data JPA repository for system settings.
 */
@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettingsEntity, UUID> {
    Optional<SystemSettingsEntity> findByKey(String key);
}
