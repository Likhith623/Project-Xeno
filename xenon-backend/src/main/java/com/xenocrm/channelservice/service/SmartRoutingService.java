package com.xenocrm.channelservice.service;

import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.channelservice.enums.MessageChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmartRoutingService {

    /**
     * Determines the most cost-effective and high-converting channel for this specific customer.
     * Email is cheap ($0.001), WhatsApp is premium ($0.05).
     */
    public MessageChannel determineOptimalChannel(CustomerEntity customer, MessageChannel defaultChannel) {
        if (customer.getMetrics() == null) {
            return defaultChannel;
        }

        // --- THE SLEEP AGENT: Channel Fatigue Interception ---
        if (customer.getChannelCooldownUntil() != null && customer.getChannelCooldownUntil().isAfter(java.time.OffsetDateTime.now())) {
            log.warn("💤 Sleep Agent: Customer {} is fatigued (cooldown until {}). Rerouting aggressively away from Email to SMS to prevent Unsubscribe.", customer.getId(), customer.getChannelCooldownUntil());
            return MessageChannel.sms;
        }

        double emailOpenRate = customer.getMetrics().getEmailOpenRate().doubleValue();
        double rfmScore = customer.getMetrics().getRfmScore().doubleValue();

        // If the user actively reads emails, save money by routing via Email
        if (emailOpenRate > 0.4) {
            log.info("SmartRouting: Customer {} is highly responsive to email. Routing via EMAIL to save budget.", customer.getId());
            return MessageChannel.email;
        }

        // If the user ignores emails but is a high-value customer, spend the budget on WhatsApp
        if (emailOpenRate < 0.1 && rfmScore > 75) {
            log.info("SmartRouting: Customer {} ignores emails but is high-value. Routing via premium WHATSAPP.", customer.getId());
            return MessageChannel.whatsapp;
        }

        return defaultChannel;
    }
}
