package com.xenocrm.customer.mapper;

import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerEntity toEntity(CustomerCreateRequestDto dto);
    void updateEntityFromDto(CustomerUpdateRequestDto dto, @MappingTarget CustomerEntity entity);
    CustomerResponseDto toResponseDto(CustomerEntity entity);
    Customer360ResponseDto to360ResponseDto(CustomerEntity customer, CustomerMetricsEntity metrics);
}
