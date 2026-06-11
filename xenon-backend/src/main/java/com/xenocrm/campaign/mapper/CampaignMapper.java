package com.xenocrm.campaign.mapper;

import com.xenocrm.campaign.dto.CampaignCreateRequestDto;
import com.xenocrm.campaign.dto.CampaignResponseDto;
import com.xenocrm.campaign.entity.CampaignEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CampaignMapper — MapStruct mapper for Campaign domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignMapper {

    @Mapping(target = "targetSegment", ignore = true) // Handled in service
    CampaignEntity toEntity(CampaignCreateRequestDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "segmentId", source = "targetSegment.id")
    CampaignResponseDto toResponseDto(CampaignEntity campaign);
}
