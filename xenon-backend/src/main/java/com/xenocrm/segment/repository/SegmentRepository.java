package com.xenocrm.segment.repository;

import com.xenocrm.segment.entity.AudienceSegmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * SegmentRepository — Spring Data JPA repository.
 */
@Repository
public interface SegmentRepository extends JpaRepository<AudienceSegmentEntity, UUID> {
}
