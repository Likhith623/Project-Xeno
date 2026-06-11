package com.xenocrm.variant.mapper;

import com.xenocrm.variant.dto.VariantCreateRequestDto;
import com.xenocrm.variant.dto.VariantResponseDto;
import com.xenocrm.variant.entity.VariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * VariantMapper — MapStruct mapper for Variant domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VariantMapper {

    @Mapping(target = "campaign", ignore = true) // Handled in service
    VariantEntity toEntity(VariantCreateRequestDto dto);

    @Mapping(target = "campaignId", source = "campaign.id")
    VariantResponseDto toResponseDto(VariantEntity entity);
}
