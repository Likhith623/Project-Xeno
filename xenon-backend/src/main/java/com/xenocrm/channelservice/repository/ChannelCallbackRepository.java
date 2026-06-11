package com.xenocrm.channelservice.repository;

import com.xenocrm.channelservice.entity.ChannelCallbackEntity;
import com.xenocrm.channelservice.enums.CallbackProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ChannelCallbackRepository — Repository for the `channel_callbacks` table.
 */
@Repository
public interface ChannelCallbackRepository extends JpaRepository<ChannelCallbackEntity, UUID> {

    List<ChannelCallbackEntity> findAllByProcessingStatus(CallbackProcessingStatus status);

    Optional<ChannelCallbackEntity> findByChannelMessageId(String channelMessageId);
}
