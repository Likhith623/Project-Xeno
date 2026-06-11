package com.xenocrm.variant.mapper;

import com.xenocrm.variant.dto.VariantCreateRequestDto;
import com.xenocrm.variant.dto.VariantResponseDto;
import com.xenocrm.variant.entity.MessageVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariantMapper {
    MessageVariantEntity toEntity(VariantCreateRequestDto dto);
    
    @Mapping(source = "campaign.id", target = "campaignId")
    VariantResponseDto toResponseDto(MessageVariantEntity entity);
}
