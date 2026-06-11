package com.xenocrm.communication.mapper;

import com.xenocrm.communication.dto.CommunicationResponseDto;
import com.xenocrm.communication.entity.CommunicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunicationMapper {
    @Mapping(source = "campaign.id", target = "campaignId")
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "attributedOrder.id", target = "attributedOrderId")
    @Mapping(source = "spawnedFollowup.id", target = "spawnedFollowupId")
    CommunicationResponseDto toResponseDto(CommunicationEntity entity);
}
