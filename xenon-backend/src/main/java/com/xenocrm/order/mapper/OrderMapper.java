package com.xenocrm.order.mapper;

import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "customer", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto dto);
    
    @Mapping(target = "customerId", source = "customer.id")
    OrderResponseDto toResponseDto(OrderEntity entity);
}
