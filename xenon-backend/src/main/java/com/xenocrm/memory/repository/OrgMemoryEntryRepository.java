package com.xenocrm.memory.repository;

import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import com.xenocrm.channelservice.enums.MessageChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;

@Repository
public interface OrgMemoryEntryRepository extends JpaRepository<OrgMemoryEntryEntity, UUID> {
    List<OrgMemoryEntryEntity> findAllBySegmentTagAndIsActiveTrue(String segmentTag);
    List<OrgMemoryEntryEntity> findAllByChannelAndIsActiveTrue(MessageChannel channel);
    List<OrgMemoryEntryEntity> findAllBySegmentTagAndChannelAndIsActiveTrue(String segmentTag, MessageChannel channel);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE OrgMemoryEntryEntity m SET m.isActive = false WHERE m.expiresAt < :now")
    void deactivateExpiredEntries(@org.springframework.data.repository.query.Param("now") OffsetDateTime now);
}
