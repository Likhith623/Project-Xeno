package com.xenocrm.product.mapper;

import com.xenocrm.product.dto.ProductCategoryResponseDto;
import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductCategoryEntity;
import com.xenocrm.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * ProductMapper — MapStruct mapper for Product domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "category", ignore = true) // Handled in service
    ProductEntity toEntity(ProductCreateRequestDto dto);

    ProductResponseDto toResponseDto(ProductEntity entity);

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    ProductCategoryResponseDto toCategoryResponseDto(ProductCategoryEntity entity);
}
