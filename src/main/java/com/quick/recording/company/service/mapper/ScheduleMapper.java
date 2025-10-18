package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.ScheduleEntity;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper extends MainMapper<ScheduleEntity, ScheduleDto> {

}
