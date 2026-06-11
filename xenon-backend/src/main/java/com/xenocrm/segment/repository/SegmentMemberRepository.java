package com.xenocrm.segment.repository;

import com.xenocrm.segment.entity.SegmentMemberEntity;
import com.xenocrm.segment.entity.SegmentMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface SegmentMemberRepository extends JpaRepository<SegmentMemberEntity, SegmentMemberId> {
    // Note: use @Query because segmentId is embedded in the composite key
    @org.springframework.data.jpa.repository.Query("SELECT sm FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    List<SegmentMemberEntity> findAllBySegmentId(@org.springframework.data.repository.query.Param("segmentId") UUID segmentId);

    @org.springframework.data.jpa.repository.Query("SELECT sm FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    org.springframework.data.domain.Page<SegmentMemberEntity> findAllBySegmentId(@org.springframework.data.repository.query.Param("segmentId") UUID segmentId, org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId")
    void deleteAllBySegmentId(@org.springframework.data.repository.query.Param("segmentId") UUID segmentId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(sm) > 0 FROM SegmentMemberEntity sm WHERE sm.id.segmentId = :segmentId AND sm.id.customerId = :customerId")
    boolean existsBySegmentIdAndCustomerId(@org.springframework.data.repository.query.Param("segmentId") UUID segmentId, @org.springframework.data.repository.query.Param("customerId") UUID customerId);
}
