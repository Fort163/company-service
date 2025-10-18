package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.service.local.MainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService extends MainService<CompanyEntity, CompanyDto> {

}
