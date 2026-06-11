package com.xenocrm.settings.mapper;

import com.xenocrm.settings.dto.SystemSettingsDto;
import com.xenocrm.settings.entity.SystemSettingsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * SystemSettingsMapper — MapStruct mapper for System Settings.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SystemSettingsMapper {

    SystemSettingsEntity toEntity(SystemSettingsDto dto);

    SystemSettingsDto toDto(SystemSettingsEntity entity);
}
