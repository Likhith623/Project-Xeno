package com.xenocrm.webhook.mapper;

import com.xenocrm.webhook.dto.WebhookConfigDto;
import com.xenocrm.webhook.dto.WebhookLogDto;
import com.xenocrm.webhook.entity.WebhookConfigEntity;
import com.xenocrm.webhook.entity.WebhookLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * WebhookMapper — MapStruct mapper for Webhook domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WebhookMapper {

    WebhookConfigEntity toConfigEntity(WebhookConfigDto dto);

    WebhookConfigDto toConfigDto(WebhookConfigEntity entity);

    @Mapping(target = "webhook", ignore = true)
    WebhookLogEntity toLogEntity(WebhookLogDto dto);

    @Mapping(target = "webhookId", source = "webhook.id")
    WebhookLogDto toLogDto(WebhookLogEntity entity);
}
