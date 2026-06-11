package com.xenocrm.order.mapper;

import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderItemRequestDto;
import com.xenocrm.order.dto.OrderItemResponseDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import com.xenocrm.order.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * OrderMapper — MapStruct mapper for Order domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "customer", ignore = true) // Handled in service
    @Mapping(target = "items", ignore = true) // Handled in service
    OrderEntity toEntity(OrderCreateRequestDto dto);

    @Mapping(target = "customerId", source = "customer.id")
    OrderResponseDto toResponseDto(OrderEntity entity);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItemEntity toItemEntity(OrderItemRequestDto dto);

    @Mapping(target = "productId", source = "product.id")
    OrderItemResponseDto toItemResponseDto(OrderItemEntity entity);
}
