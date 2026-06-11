package com.xenocrm.campaign.mapper;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.entity.CampaignMetricsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CampaignMapper — MapStruct mapper for Campaign domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignMapper {

    @Mapping(target = "segment", ignore = true) // Handled in service
    CampaignEntity toEntity(CampaignCreateRequestDto dto);

    @Mapping(target = "id", source = "campaign.id")
    @Mapping(target = "segmentId", source = "campaign.segment.id")
    @Mapping(target = "totalTargeted", source = "metrics.totalTargeted")
    @Mapping(target = "totalSent", source = "metrics.totalSent")
    @Mapping(target = "totalDelivered", source = "metrics.totalDelivered")
    @Mapping(target = "totalFailed", source = "metrics.totalFailed")
    @Mapping(target = "totalOpened", source = "metrics.totalOpened")
    @Mapping(target = "totalClicked", source = "metrics.totalClicked")
    @Mapping(target = "totalOptOuts", source = "metrics.totalOptOuts")
    @Mapping(target = "totalConversions", source = "metrics.totalConversions")
    @Mapping(target = "revenueGenerated", source = "metrics.revenueGenerated")
    @Mapping(target = "lastMetricsUpdatedAt", source = "metrics.lastUpdatedAt")
    CampaignResponseDto toResponseDto(CampaignEntity campaign, CampaignMetricsEntity metrics);
}
