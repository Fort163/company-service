package com.quick.recording.company.service.controller;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.SearchCompanyDto;
import com.quick.recording.gateway.service.company.CompanyServiceCompanyApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class CompanyControllerImpl implements CompanyServiceCompanyApi {


    @Override
    public ResponseEntity<CompanyDto> byUuid(UUID uuid) {
        return null;
    }

    @Override
    public Page<CompanyDto> list(SearchCompanyDto searchCompanyDto, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<CompanyDto> post(CompanyDto companyDto) {
        companyDto.getUuid().toString();
        return null;
    }

    @Override
    public ResponseEntity<CompanyDto> patch(CompanyDto companyDto) {
        return null;
    }

    @Override
    public ResponseEntity<CompanyDto> put(CompanyDto companyDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> delete(UUID uuid) {
        return null;
    }
}
