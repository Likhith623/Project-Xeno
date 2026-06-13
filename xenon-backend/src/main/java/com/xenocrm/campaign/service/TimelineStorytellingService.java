package com.xenocrm.campaign.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimelineStorytellingService {

    private final CampaignRepository campaignRepository;

    public List<String> getCampaignTimeline(UUID campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        List<String> timeline = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");

        if (campaign.getCreatedAt() != null) {
            timeline.add(campaign.getCreatedAt().format(formatter) + " - Campaign '" + campaign.getName() + "' was created.");
        }
        
        if (campaign.getUpdatedAt() != null && campaign.getStatus().name().equals("RUNNING")) {
            timeline.add(campaign.getUpdatedAt().format(formatter) + " - Execution started. Targeting " + 
                         (campaign.getTargetSegment() != null ? campaign.getTargetSegment().getName() : "segment") + ".");
        }

        if (campaign.getTotalSent() > 0) {
            timeline.add("Sent " + campaign.getTotalSent() + " messages across channels.");
        }
        if (campaign.getTotalDelivered() > 0) {
            timeline.add("Successfully delivered to " + campaign.getTotalDelivered() + " recipients (" + 
                         String.format("%.1f%%", (campaign.getTotalDelivered() * 100.0 / Math.max(1, campaign.getTotalSent()))) + " delivery rate).");
        }
        if (campaign.getTotalOpened() > 0) {
            timeline.add(campaign.getTotalOpened() + " users opened the message.");
        }
        if (campaign.getTotalClicked() > 0) {
            timeline.add(campaign.getTotalClicked() + " users engaged and clicked the call-to-action.");
        }
        if (campaign.getTotalConverted() > 0) {
            timeline.add(campaign.getTotalConverted() + " users converted, generating " + 
                         (campaign.getRevenueAttributed() != null ? "₹" + campaign.getRevenueAttributed() : "revenue") + ".");
        }

        if (campaign.getStatus().name().equals("COMPLETED")) {
            timeline.add("Campaign finished successfully.");
        }

        return timeline;
    }
}
