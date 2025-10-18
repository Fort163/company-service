package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.ProfessionEntity;
import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProfessionMapper extends MainMapper<ProfessionEntity, ProfessionDto> {
}
