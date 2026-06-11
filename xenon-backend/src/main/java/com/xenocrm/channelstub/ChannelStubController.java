package com.xenocrm.channelstub;

import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import com.xenocrm.channelservice.dto.ChannelSendResponseDto;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * ChannelStubController — Mock API endpoint for receiving messages from the CRM.
 * Layer: Controller
 */
@RestController
@RequestMapping("/api/v1/stub")
@Profile("stub")
public class ChannelStubController {

    private final ChannelStubSimulationService simulationService;

    public ChannelStubController(ChannelStubSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChannelSendResponseDto> receiveMessage(@RequestBody ChannelSendRequestDto requestDto) {
        String messageId = "msg_" + UUID.randomUUID().toString();
        
        // Start async simulation of delivery and interaction callbacks
        simulationService.simulateLifecycleEvents(messageId, requestDto);

        return ResponseEntity.ok(ChannelSendResponseDto.builder()
                .channelMessageId(messageId)
                .success(true)
                .build());
    }
}
