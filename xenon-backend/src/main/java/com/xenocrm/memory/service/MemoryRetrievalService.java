package com.xenocrm.memory.service;

import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import com.xenocrm.memory.repository.OrgMemoryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MemoryRetrievalService — Retrieves context for the AI when generating campaigns.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
public class MemoryRetrievalService {

    private final OrgMemoryEntryRepository memoryRepository;
    private final com.xenocrm.memory.mapper.OrgMemoryMapper memoryMapper;

    public org.springframework.data.domain.Page<com.xenocrm.memory.dto.OrgMemoryEntryDto> getAllMemories(org.springframework.data.domain.Pageable pageable) {
        return memoryRepository.findAll(pageable).map(memoryMapper::toDto);
    }

    public org.springframework.data.domain.Page<com.xenocrm.memory.dto.OrgMemoryEntryDto> getMemoriesByQuery(
            String segmentTag, com.xenocrm.channelservice.enums.MessageChannel channel, org.springframework.data.domain.Pageable pageable) {
        if (segmentTag != null && channel != null) {
            return new org.springframework.data.domain.PageImpl<>(
                    memoryRepository.findAllBySegmentTagAndChannelAndIsActiveTrue(segmentTag, channel), pageable, 100).map(memoryMapper::toDto);
        } else if (segmentTag != null) {
            return new org.springframework.data.domain.PageImpl<>(
                    memoryRepository.findAllBySegmentTagAndIsActiveTrue(segmentTag), pageable, 100).map(memoryMapper::toDto);
        } else if (channel != null) {
            return new org.springframework.data.domain.PageImpl<>(
                    memoryRepository.findAllByChannelAndIsActiveTrue(channel), pageable, 100).map(memoryMapper::toDto);
        }
        return getAllMemories(pageable);
    }

    public String buildContextPromptForSegment(String segmentTag) {
        List<OrgMemoryEntryEntity> memories = memoryRepository.findAllBySegmentTagAndIsActiveTrue(segmentTag);

        if (memories.isEmpty()) {
            return "No prior organizational memory available for segment: " + segmentTag;
        }

        return "Organizational Memory for " + segmentTag + ":\n" +
                memories.stream()
                        .map(m -> String.format("- [%s / %s] %s (Confidence: %.2f)",
                                m.getLearningType(), m.getChannel(), m.getLearningSummary(), m.getConfidence()))
                        .collect(Collectors.joining("\n"));
    }

    public String getNaturalLanguageAnswer(String query, com.xenocrm.agent.service.AgentLlmGatewayService llmGatewayService) {
        List<OrgMemoryEntryEntity> memories = memoryRepository.findAll();
        
        String memoryContext = memories.stream()
                .map(m -> String.format("- Tag: %s, Channel: %s, Type: %s, Summary: %s",
                        m.getSegmentTag(), m.getChannel(), m.getLearningType(), m.getLearningSummary()))
                .collect(Collectors.joining("\n"));

        String prompt = "You are the Organizational Memory Retrieval AI. The user asked: " + query + "\n" +
                        "Here is the database of all historical learnings:\n" + memoryContext + "\n" +
                        "Answer the user's question concisely based ONLY on these memories.";
        
        return llmGatewayService.callGemini(prompt);
    }
}
