package com.xenocrm.report.repository;

import com.xenocrm.report.entity.ReportConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * ReportConfigRepository — Spring Data JPA repository for report configs.
 */
@Repository
public interface ReportConfigRepository extends JpaRepository<ReportConfigEntity, UUID> {
}
