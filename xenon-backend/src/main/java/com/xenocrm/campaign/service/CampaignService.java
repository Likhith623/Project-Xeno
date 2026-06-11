package com.xenocrm.campaign.service;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.mapper.CampaignMapper;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * CampaignService — Handles campaign creation and retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final SegmentRepository segmentRepository;
    private final CampaignMapper campaignMapper;

    @Transactional
    public CampaignResponseDto createCampaign(CampaignCreateRequestDto request) {
        log.debug("Creating new campaign: {}", request.getName());

        AudienceSegmentEntity segment = segmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", request.getSegmentId()));

        CampaignEntity campaign = campaignMapper.toEntity(request);
        campaign.setTargetSegment(segment);
        
        if (campaign.getStatus() == null) {
            campaign.setStatus(CampaignStatus.DRAFT);
        }

        CampaignEntity savedCampaign = campaignRepository.save(campaign);

        return campaignMapper.toResponseDto(savedCampaign);
    }

    @Transactional(readOnly = true)
    public CampaignResponseDto getCampaignById(UUID id) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));
        return campaignMapper.toResponseDto(campaign);
    }
}
