package com.xenocrm.memory.service;

import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import com.xenocrm.memory.enums.LearningType;
import com.xenocrm.memory.repository.OrgMemoryRepository;
import com.xenocrm.variant.enums.MessageChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * MemoryPersistenceService — Stores new learnings discovered by the AI.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
public class MemoryPersistenceService {

    private final OrgMemoryRepository memoryRepository;

    @Transactional
    public OrgMemoryEntryEntity recordLearning(String segmentTag, MessageChannel channel, LearningType type, 
                                               String summary, BigDecimal confidence, BigDecimal avgLift, 
                                               Map<String, Object> signals) {
        
        OrgMemoryEntryEntity entry = OrgMemoryEntryEntity.builder()
                .segmentTag(segmentTag)
                .channel(channel)
                .learningType(type)
                .learningSummary(summary)
                .confidence(confidence)
                .evidenceCount(1)
                .avgLift(avgLift)
                .winningCopySignals(signals)
                .build();

        return memoryRepository.save(entry);
    }
}
