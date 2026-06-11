package com.xenocrm.segment.repository;

import com.xenocrm.segment.entity.SegmentMemberEntity;
import com.xenocrm.segment.entity.SegmentMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * SegmentMemberRepository -- JPA repository for SegmentMemberEntity (composite PK).
 * Layer: Repository
 */
public interface SegmentMemberRepository extends JpaRepository<SegmentMemberEntity, SegmentMemberId> {
    /** Finds all members of a segment. */
    @Query("SELECT sm FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    List<SegmentMemberEntity> findAllBySegmentId(@Param("segmentId") UUID segmentId);
    /** Finds all members of a segment, paginated. */
    @Query("SELECT sm FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    Page<SegmentMemberEntity> findAllBySegmentId(@Param("segmentId") UUID segmentId, Pageable pageable);
    /** Deletes all members of a segment (before re-evaluation). */
    @Modifying @Transactional
    @Query("DELETE FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    void deleteAllBySegmentId(@Param("segmentId") UUID segmentId);
    /** Checks if a specific customer is a member of a specific segment. */
    @Query("SELECT COUNT(sm) > 0 FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId AND sm.id.customerId = :customerId")
    boolean existsBySegmentIdAndCustomerId(@Param("segmentId") UUID segmentId, @Param("customerId") UUID customerId);
}
