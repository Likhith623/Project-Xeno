package com.xenocrm.customer.mapper;

import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * CustomerMapper — MapStruct mapper for Customer domain.
 * Layer: Mapper
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerEntity toEntity(CustomerCreateRequestDto dto);

    void updateEntityFromDto(CustomerUpdateRequestDto dto, @MappingTarget CustomerEntity entity);

    CustomerResponseDto toResponseDto(CustomerEntity entity);

    @Mapping(target = "id", source = "customer.id")
    @Mapping(target = "externalId", source = "customer.externalId")
    @Mapping(target = "email", source = "customer.email")
    @Mapping(target = "phone", source = "customer.phone")
    @Mapping(target = "whatsappNumber", source = "customer.whatsappNumber")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "gender", source = "customer.gender")
    @Mapping(target = "dateOfBirth", source = "customer.dateOfBirth")
    @Mapping(target = "city", source = "customer.city")
    @Mapping(target = "state", source = "customer.state")
    @Mapping(target = "country", source = "customer.country")
    @Mapping(target = "tags", source = "customer.tags")
    @Mapping(target = "customAttributes", source = "customer.customAttributes")
    @Mapping(target = "preferredChannel", source = "customer.preferredChannel")
    @Mapping(target = "optOutChannels", source = "customer.optOutChannels")
    @Mapping(target = "isGloballyOptedOut", source = "customer.globallyOptedOut")
    @Mapping(target = "createdAt", source = "customer.createdAt")
    @Mapping(target = "updatedAt", source = "customer.updatedAt")
    @Mapping(target = "recencyDays", source = "metrics.recencyDays")
    @Mapping(target = "frequency", source = "metrics.frequency")
    @Mapping(target = "monetaryTotal", source = "metrics.monetaryTotal")
    @Mapping(target = "monetaryAvgOrder", source = "metrics.monetaryAvgOrder")
    @Mapping(target = "rfmScore", source = "metrics.rfmScore")
    @Mapping(target = "totalOrdersLast30d", source = "metrics.totalOrdersLast30d")
    @Mapping(target = "totalOrdersLast90d", source = "metrics.totalOrdersLast90d")
    @Mapping(target = "avgDaysBetweenOrders", source = "metrics.avgDaysBetweenOrders")
    @Mapping(target = "favouriteCategoryId", source = "metrics.favouriteCategoryId")
    @Mapping(target = "favouriteChannel", source = "metrics.favouriteChannel")
    @Mapping(target = "clvPredicted", source = "metrics.clvPredicted")
    @Mapping(target = "churnProbability", source = "metrics.churnProbability")
    @Mapping(target = "emailOpenRate", source = "metrics.emailOpenRate")
    @Mapping(target = "emailClickRate", source = "metrics.emailClickRate")
    @Mapping(target = "whatsappReadRate", source = "metrics.whatsappReadRate")
    @Mapping(target = "smsClickRate", source = "metrics.smsClickRate")
    @Mapping(target = "lastComputedAt", source = "metrics.lastComputedAt")
    Customer360ResponseDto to360ResponseDto(CustomerEntity customer, CustomerMetricsEntity metrics);
}
