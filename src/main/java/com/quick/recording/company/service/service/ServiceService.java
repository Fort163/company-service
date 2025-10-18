package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.ServiceEntity;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.main.service.local.MainService;

public interface ServiceService extends MainService<ServiceEntity, ServiceDto> {
}
