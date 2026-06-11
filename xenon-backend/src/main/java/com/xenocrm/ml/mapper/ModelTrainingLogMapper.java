package com.xenocrm.ml.mapper;

import com.xenocrm.ml.dto.ModelTrainingLogDto;
import com.xenocrm.ml.entity.ModelTrainingLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * ModelTrainingLogMapper — MapStruct mapper for ML domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelTrainingLogMapper {

    ModelTrainingLogEntity toEntity(ModelTrainingLogDto dto);

    ModelTrainingLogDto toDto(ModelTrainingLogEntity entity);
}
