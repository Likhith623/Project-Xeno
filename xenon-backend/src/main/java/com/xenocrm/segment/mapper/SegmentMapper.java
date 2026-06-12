package com.xenocrm.segment.mapper;

import com.xenocrm.segment.dto.SegmentCreateRequestDto;
import com.xenocrm.segment.dto.SegmentResponseDto;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SegmentMapper {
    AudienceSegmentEntity toEntity(SegmentCreateRequestDto dto);
    SegmentResponseDto toResponseDto(AudienceSegmentEntity entity);
    
    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(com.xenocrm.segment.dto.SegmentUpdateRequestDto dto, @org.mapstruct.MappingTarget AudienceSegmentEntity entity);
}
