package com.xenocrm.communication.mapper;

import com.xenocrm.communication.dto.CommunicationLogResponseDto;
import com.xenocrm.communication.entity.CommunicationLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CommunicationLogMapper — MapStruct mapper for Communication domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommunicationLogMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "campaignId", source = "campaign.id")
    @Mapping(target = "variantId", source = "variant.id")
    CommunicationLogResponseDto toResponseDto(CommunicationLogEntity entity);
}
