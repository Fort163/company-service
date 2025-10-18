package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.EmployeeEntity;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.main.service.local.MainService;

public interface EmployeeService extends MainService<EmployeeEntity, EmployeeDto> {
}
