package com.quick.recording.company.service.service.impl;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.company.service.mapper.CompanyMapper;
import com.quick.recording.company.service.repository.CompanyRepository;
import com.quick.recording.company.service.service.CompanyService;
import com.quick.recording.gateway.dto.company.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    @Override
    public List<CompanyDto> getAll() {
        List<CompanyEntity> all = companyRepository.findAll();
        return all.stream().map(companyMapper::companyEntityToCompanyDto).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID uuid) {

    }
    @Override
    public CompanyDto getById(UUID uuid) {

        return null;
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        return companyMapper.companyEntityToCompanyDto(companyRepository.save(companyMapper.companyDtoToCompanyEntity(companyDto)));
    }

    @Override
    public CompanyDto update(UUID uuid, CompanyDto companyDto) {
        return null;
    }
}
