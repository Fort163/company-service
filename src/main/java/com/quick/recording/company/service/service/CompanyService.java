package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.CompanyDto;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    List<CompanyDto> getAll();

    void delete(UUID uuid);

    CompanyDto getById(UUID uuid);

    CompanyDto save(CompanyDto companyDto);

    CompanyDto update(UUID uuid,CompanyDto companyDto);
}
