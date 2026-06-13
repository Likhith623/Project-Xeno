package com.xenocrm.campaign.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import com.xenocrm.channelservice.enums.MessageChannel;
import com.xenocrm.channelservice.service.ChannelDispatchService;
import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.communication.repository.CommunicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JourneyFallbackService {

    private final CommunicationRepository communicationRepository;
    private final ChannelDispatchService channelDispatchService;
    private final AgentLlmGatewayService llmGatewayService;

    // Runs every 4 hours to check for unengaged users
    @Scheduled(fixedRate = 14400000)
    public void executeFallbackJourney() {
        log.info("Running AI Multi-Channel Journey Builder...");

        // Find emails sent > 24 hours ago that were delivered but never opened/clicked
        OffsetDateTime threshold = OffsetDateTime.now().minusHours(24);
        List<CommunicationEntity> unengagedComms = communicationRepository.findAll().stream()
                .filter(c -> c.getChannel() == MessageChannel.email)
                .filter(c -> c.getStatus() == CommunicationStatus.DELIVERED)
                .filter(c -> c.getCreatedAt().isBefore(threshold))
                .toList();

        for (CommunicationEntity comm : unengagedComms) {
            log.info("User {} ignored email. Triggering Fallback SMS journey.", comm.getCustomer().getId());
            
            String prompt = "You are an AI Journey Builder. The user ignored the following email:\n" +
                            comm.getPersonalisedBody() + "\n\n" +
                            "Generate a short, punchy SMS (max 160 chars) to follow up and create urgency. " +
                            "Respond ONLY with the exact SMS text.";

            try {
                String fallbackSmsText = llmGatewayService.callGemini(prompt).trim();

                CommunicationEntity fallbackComm = CommunicationEntity.builder()
                        .campaign(comm.getCampaign())
                        .variant(comm.getVariant()) // Tie to original variant for tracking
                        .customer(comm.getCustomer())
                        .channel(MessageChannel.sms)
                        .status(CommunicationStatus.PENDING)
                        .recipientAddress(comm.getCustomer().getPhone())
                        .personalisedSubject("Follow-up SMS")
                        .personalisedBody(fallbackSmsText)
                        .build();

                communicationRepository.save(fallbackComm);

                ChannelSendRequestDto req = ChannelSendRequestDto.builder()
                        .recipientAddress(comm.getCustomer().getPhone())
                        .communicationId(fallbackComm.getId())
                        .channel(MessageChannel.sms)
                        .subject("Follow-up SMS")
                        .body(fallbackSmsText)
                        .build();

                channelDispatchService.dispatchMessage(req);

                // Mark original communication as FALLBACK_TRIGGERED so we don't process it again
                comm.setStatus(CommunicationStatus.FAILED); // or a custom status like FALLBACK_TRIGGERED
                communicationRepository.save(comm);

            } catch (Exception e) {
                log.error("Fallback journey failed for communication {}", comm.getId(), e);
            }
        }
    }
}
