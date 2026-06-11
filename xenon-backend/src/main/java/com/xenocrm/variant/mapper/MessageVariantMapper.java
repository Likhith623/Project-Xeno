package com.xenocrm.variant.mapper;

import com.xenocrm.variant.dto.MessageVariantCreateRequestDto;
import com.xenocrm.variant.dto.MessageVariantResponseDto;
import com.xenocrm.variant.entity.MessageVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MessageVariantMapper -- MapStruct mapper for MessageVariant domain.
 * Layer: Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageVariantMapper {
    /** Converts a create request DTO to a MessageVariantEntity (campaign set in service). */
    @Mapping(target = "campaign", ignore = true)
    MessageVariantEntity toEntity(MessageVariantCreateRequestDto dto);
    /** Converts a MessageVariantEntity to a standard response DTO. */
    @Mapping(target = "campaignId", source = "campaign.id")
    MessageVariantResponseDto toResponseDto(MessageVariantEntity entity);
}
