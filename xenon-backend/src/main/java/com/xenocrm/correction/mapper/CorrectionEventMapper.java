package com.xenocrm.correction.mapper;

import com.xenocrm.correction.dto.CorrectionEventResponseDto;
import com.xenocrm.correction.entity.CorrectionEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * CorrectionEventMapper — Maps between CorrectionEventEntity and CorrectionEventResponseDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CorrectionEventMapper {

    @Mapping(source = "campaign.id", target = "campaignId")
    @Mapping(source = "oldVariant.id", target = "oldVariantId")
    @Mapping(source = "newVariant.id", target = "newVariantId")
    CorrectionEventResponseDto toResponseDto(CorrectionEventEntity entity);
}
