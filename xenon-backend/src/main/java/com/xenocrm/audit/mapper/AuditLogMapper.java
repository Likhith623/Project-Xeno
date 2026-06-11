package com.xenocrm.audit.mapper;

import com.xenocrm.audit.dto.AuditLogDto;
import com.xenocrm.audit.entity.AuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * AuditLogMapper — MapStruct mapper for Audit domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuditLogMapper {

    AuditLogEntity toEntity(AuditLogDto dto);

    AuditLogDto toDto(AuditLogEntity entity);
}
