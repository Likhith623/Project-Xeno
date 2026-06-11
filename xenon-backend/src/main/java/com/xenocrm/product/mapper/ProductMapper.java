package com.xenocrm.product.mapper;

import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductCreateRequestDto dto);
    
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponseDto toResponseDto(ProductEntity entity);
}
