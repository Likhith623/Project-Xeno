package com.xenocrm.campaign.mapper;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.entity.CampaignEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CampaignMapper -- MapStruct mapper for Campaign domain.
 * Layer: Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignMapper {
    /** Converts a create request DTO to a CampaignEntity (segment/parent set in service). */
    @Mapping(target = "targetSegment", ignore = true)
    @Mapping(target = "parentCampaign", ignore = true)
    CampaignEntity toEntity(CampaignCreateRequestDto dto);
    /** Converts a CampaignEntity to a standard response DTO. */
    @Mapping(target = "segmentId",        source = "targetSegment.id")
    @Mapping(target = "segmentName",      source = "targetSegment.name")
    @Mapping(target = "parentCampaignId", source = "parentCampaign.id")
    CampaignResponseDto toResponseDto(CampaignEntity campaign);
}
