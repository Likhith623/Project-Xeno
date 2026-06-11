package com.xenocrm.event.mapper;

import com.xenocrm.event.dto.EventCreateRequestDto;
import com.xenocrm.event.dto.EventResponseDto;
import com.xenocrm.event.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * EventMapper — MapStruct mapper for Event domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    @Mapping(target = "customer", ignore = true) // Handled in service
    EventEntity toEntity(EventCreateRequestDto dto);

    @Mapping(target = "customerId", source = "customer.id")
    EventResponseDto toResponseDto(EventEntity entity);
}
