package com.xenocrm.audit.mapper;

import com.xenocrm.audit.dto.AuditLogResponseDto;
import com.xenocrm.audit.entity.AuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * AuditLogMapper — Maps between AuditLogEntity and AuditLogResponseDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditLogMapper {
    AuditLogResponseDto toResponseDto(AuditLogEntity entity);
}
