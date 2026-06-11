package com.xenocrm.segment.repository;

import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface AudienceSegmentRepository extends JpaRepository<AudienceSegmentEntity, UUID> {
    List<AudienceSegmentEntity> findAllByStatus(SegmentStatus status);
    List<AudienceSegmentEntity> findAllByCreatedByAgentTrue();

    /** Calls the fn_evaluate_segment stored function. Returns new customer_count. */
    @org.springframework.data.jpa.repository.Query(value="SELECT fn_evaluate_segment(CAST(:segmentId AS uuid))", nativeQuery=true)
    Integer callEvaluateSegmentFunction(@org.springframework.data.repository.query.Param("segmentId") String segmentId);
}
