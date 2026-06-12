package com.xenocrm.product.mapper;

import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * ProductMapper -- MapStruct mapper for Product domain.
 * Layer: Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    /** Converts a create request DTO to a ProductEntity (category set in service). */
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductCreateRequestDto dto);
    /** Converts a ProductEntity to a standard response DTO. */
    @Mapping(target = "categoryId",   source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDto toResponseDto(ProductEntity entity);

    com.xenocrm.product.dto.ProductCategoryResponseDto toCategoryResponseDto(com.xenocrm.product.entity.ProductCategoryEntity entity);
}
