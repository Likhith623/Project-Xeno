package com.xenocrm.channelstub;

import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * ChannelStubOutcomeGenerator — Determines probabilistic outcomes for sent messages.
 * Layer: Component
 */
@Component
@Profile("stub")
public class ChannelStubOutcomeGenerator {

    private final Random random = new Random();

    public boolean shouldDeliver(ChannelSendRequestDto requestDto) {
        // 95% delivery rate
        return random.nextDouble() < 0.95;
    }

    public boolean shouldOpen(ChannelSendRequestDto requestDto) {
        // 40% open rate for delivered
        return random.nextDouble() < 0.40;
    }

    public boolean shouldClick(ChannelSendRequestDto requestDto) {
        // 10% click rate for opened
        return random.nextDouble() < 0.10;
    }

    public boolean shouldConvert(ChannelSendRequestDto requestDto) {
        // 2% conversion rate for clicked
        return random.nextDouble() < 0.02;
    }
    
    public boolean shouldUnsubscribe(ChannelSendRequestDto requestDto) {
        // 1% unsubscribe rate for opened
        return random.nextDouble() < 0.01;
    }
}
