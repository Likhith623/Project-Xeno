package com.xenocrm.campaign.service;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.mapper.CampaignMapper;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * CampaignService — Handles campaign creation and retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final AudienceSegmentRepository segmentRepository;
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

    @Transactional(readOnly = true)
    public Page<CampaignResponseDto> getAllCampaigns(Pageable pageable) {
        return campaignRepository.findAll(pageable).map(campaignMapper::toResponseDto);
    }

    @Transactional
    public CampaignResponseDto updateCampaignStatus(UUID id, String newStatus) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));
        campaign.setStatus(CampaignStatus.valueOf(newStatus.toUpperCase()));
        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponseDto updateCampaignDetails(UUID id, com.xenocrm.campaign.dto.CampaignUpdateRequestDto request) {
        CampaignEntity campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));
        
        if (request.getName() != null) {
            campaign.setName(request.getName());
        }
        if (request.getDescription() != null) {
            campaign.setDescription(request.getDescription());
        }
        if (request.getGoal() != null) {
            campaign.setGoal(request.getGoal());
        }
        if (request.getTargetSegmentId() != null) {
            AudienceSegmentEntity segment = segmentRepository.findById(request.getTargetSegmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", request.getTargetSegmentId()));
            campaign.setTargetSegment(segment);
        }

        return campaignMapper.toResponseDto(campaignRepository.save(campaign));
    }

    @Transactional(readOnly = true)
    public com.xenocrm.campaign.dto.CampaignPerformanceDto getCampaignPerformance(UUID id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campaign", "id", id);
        }
        com.xenocrm.campaign.repository.CampaignPerformanceProjection row = campaignRepository.findPerformanceSummaryById(id.toString())
                .orElseThrow(() -> new ResourceNotFoundException("CampaignPerformance", "id", id));
        
        return com.xenocrm.campaign.dto.CampaignPerformanceDto.builder()
                .id(row.getId())
                .name(row.getName())
                .status(row.getStatus())
                .goal(row.getGoal())
                .scheduledAt(row.getScheduledAt() != null ? java.time.OffsetDateTime.ofInstant(row.getScheduledAt(), java.time.ZoneId.of("Asia/Kolkata")) : null)
                .startedAt(row.getStartedAt() != null ? java.time.OffsetDateTime.ofInstant(row.getStartedAt(), java.time.ZoneId.of("Asia/Kolkata")) : null)
                .completedAt(row.getCompletedAt() != null ? java.time.OffsetDateTime.ofInstant(row.getCompletedAt(), java.time.ZoneId.of("Asia/Kolkata")) : null)
                .createdByAgent(row.getCreatedByAgent())
                .totalSent(row.getTotalSent() != null ? row.getTotalSent() : 0)
                .totalDelivered(row.getTotalDelivered() != null ? row.getTotalDelivered() : 0)
                .totalFailed(row.getTotalFailed() != null ? row.getTotalFailed() : 0)
                .totalOpened(row.getTotalOpened() != null ? row.getTotalOpened() : 0)
                .totalRead(row.getTotalRead() != null ? row.getTotalRead() : 0)
                .totalClicked(row.getTotalClicked() != null ? row.getTotalClicked() : 0)
                .totalConverted(row.getTotalConverted() != null ? row.getTotalConverted() : 0)
                .revenueAttributed(row.getRevenueAttributed())
                .deliveryRatePct(row.getDeliveryRatePct())
                .failureRatePct(row.getFailureRatePct())
                .openRatePct(row.getOpenRatePct())
                .ctrPct(row.getCtrPct())
                .conversionRatePct(row.getConversionRatePct())
                .optOutRatePct(row.getOptOutRatePct())
                .segmentName(row.getSegmentName())
                .segmentSize(row.getSegmentSize() != null ? row.getSegmentSize() : 0)
                .build();
    }

    @Transactional(readOnly = true)
    public java.util.List<com.xenocrm.campaign.dto.OptOutAlertDto> getOptOutAlerts() {
        java.util.List<com.xenocrm.campaign.repository.OptOutAlertProjection> rows = campaignRepository.findAllOptOutAlerts();
        return rows.stream().map(row -> com.xenocrm.campaign.dto.OptOutAlertDto.builder()
                .campaignId(row.getCampaignId())
                .campaignName(row.getCampaignName())
                .optOutRateThreshold(row.getOptOutRateThreshold())
                .currentOptOutRatePct(row.getCurrentOptOutRatePct())
                .alertLevel(row.getAlertLevel())
                .build()
        ).collect(java.util.stream.Collectors.toList());
    }
}
