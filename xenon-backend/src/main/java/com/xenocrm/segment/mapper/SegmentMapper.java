package com.xenocrm.segment.mapper;

import com.xenocrm.segment.dto.SegmentCreateRequestDto;
import com.xenocrm.segment.dto.SegmentResponseDto;
import com.xenocrm.segment.entity.SegmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * SegmentMapper — MapStruct mapper for Segment domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SegmentMapper {

    SegmentEntity toEntity(SegmentCreateRequestDto dto);

    SegmentResponseDto toResponseDto(SegmentEntity entity);
}
