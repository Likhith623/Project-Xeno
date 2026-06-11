package com.xenocrm.report.mapper;

import com.xenocrm.report.dto.ReportConfigDto;
import com.xenocrm.report.entity.ReportConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * ReportConfigMapper — MapStruct mapper for Report domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReportConfigMapper {

    ReportConfigEntity toEntity(ReportConfigDto dto);

    ReportConfigDto toDto(ReportConfigEntity entity);
}
