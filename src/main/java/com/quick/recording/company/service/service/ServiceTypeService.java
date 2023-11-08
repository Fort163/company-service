package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.GeoPositionDto;
import com.quick.recording.gateway.dto.company.ServiceTypeDto;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeService {
    List<ServiceTypeDto> getAll();

    void delete(UUID uuid);

    ServiceTypeDto getById(UUID uuid);

    ServiceTypeDto save(ServiceTypeDto serviceTypeDto);

    ServiceTypeDto update(UUID uuid,ServiceTypeDto serviceTypeDto);
}
