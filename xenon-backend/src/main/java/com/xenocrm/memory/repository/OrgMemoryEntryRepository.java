package com.xenocrm.memory.repository;

import com.xenocrm.channelservice.enums.MessageChannel;
import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * OrgMemoryEntryRepository -- JPA repository for OrgMemoryEntryEntity.
 * Layer: Repository
 */
public interface OrgMemoryEntryRepository extends JpaRepository<OrgMemoryEntryEntity, UUID> {
    /** Finds all active memory entries for a specific segment tag. */
    List<OrgMemoryEntryEntity> findAllBySegmentTagAndIsActiveTrue(String segmentTag);
    /** Finds all active memory entries for a specific channel. */
    List<OrgMemoryEntryEntity> findAllByChannelAndIsActiveTrue(MessageChannel channel);
    /** Finds all active memory entries for a specific segment tag and channel. */
    List<OrgMemoryEntryEntity> findAllBySegmentTagAndChannelAndIsActiveTrue(String segmentTag, MessageChannel channel);
    /** Deactivates all memory entries whose expiry date has passed. */
    @Modifying @Transactional
    @Query("UPDATE OrgMemoryEntryEntity m SET m.isActive = false WHERE m.expiresAt < :now")
    void deactivateExpiredEntries(@Param("now") OffsetDateTime now);
}
