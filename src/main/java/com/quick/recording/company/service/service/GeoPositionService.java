package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.GeoObjectDto;
import com.quick.recording.gateway.dto.company.GeoPositionDto;

import java.util.List;
import java.util.UUID;

public interface GeoPositionService {
    List<GeoPositionDto> getAll();

    void delete(UUID uuid);

    GeoPositionDto getById(UUID uuid);

    GeoPositionDto save(GeoPositionDto geoPositionDto);

    GeoPositionDto update(UUID uuid,GeoPositionDto geoPositionDto);
}
