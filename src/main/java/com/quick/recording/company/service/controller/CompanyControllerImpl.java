package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.company.service.repository.dto.CompanyDtoRepository;
import com.quick.recording.company.service.service.CompanyService;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceCompanyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/company")
public class  CompanyControllerImpl
        extends CacheableMainControllerAbstract<CompanyDto, CompanyEntity, CompanyDtoRepository, CompanyService>
        implements CompanyServiceCompanyApi {

    @Autowired
    public CompanyControllerImpl(CompanyService service, CompanyDtoRepository repository) {
        super(service, repository);
    }

}
