package com.xenocrm.segment.repository;

import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

/**
 * AudienceSegmentRepository -- JPA repository for AudienceSegmentEntity.
 * Layer: Repository
 */
public interface AudienceSegmentRepository extends JpaRepository<AudienceSegmentEntity, UUID> {
    /** Returns all segments with the given status. */
    List<AudienceSegmentEntity> findAllByStatus(SegmentStatus status);
    /** Returns all segments created by the Sovereign Agent. */
    List<AudienceSegmentEntity> findAllByCreatedByAgentTrue();
    /** Calls the fn_evaluate_segment stored function. Returns new customer_count. */
    @Query(value = "SELECT fn_evaluate_segment(CAST(:segmentId AS uuid))", nativeQuery = true)
    Integer callEvaluateSegmentFunction(@Param("segmentId") String segmentId);
}
