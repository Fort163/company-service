package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.ServiceEntity;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper extends MainMapper<ServiceEntity, ServiceDto> {
}
