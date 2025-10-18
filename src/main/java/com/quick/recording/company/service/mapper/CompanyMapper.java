package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {ScheduleMapper.class, GeocoderMapper.class, ActivityMapper.class, ServiceMapper.class})
public interface CompanyMapper extends MainMapper<CompanyEntity, CompanyDto> {

}
