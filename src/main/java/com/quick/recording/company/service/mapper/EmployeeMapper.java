package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.EmployeeEntity;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {CompanyMapper.class, ProfessionMapper.class, ServiceMapper.class})
public interface EmployeeMapper extends MainMapper<EmployeeEntity, EmployeeDto> {
}
