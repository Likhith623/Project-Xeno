package com.xenocrm.order.mapper;

import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * OrderMapper -- MapStruct mapper for Order domain.
 * Layer: Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    /** Converts a create request DTO to an OrderEntity (customer set in service). */
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto dto);
    /** Converts an OrderEntity to a standard response DTO. */
    @Mapping(target = "customerId", source = "customer.id")
    OrderResponseDto toResponseDto(OrderEntity entity);
}
