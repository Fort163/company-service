package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.GeoObjectDto;

import java.util.List;
import java.util.UUID;

public interface GeoObjectService {
    List<GeoObjectDto> getAll();

    void delete(UUID uuid);

    GeoObjectDto getById(UUID uuid);

    GeoObjectDto save(GeoObjectDto geoObjectDto);

    GeoObjectDto update(UUID uuid,GeoObjectDto geoObjectDto);
}
