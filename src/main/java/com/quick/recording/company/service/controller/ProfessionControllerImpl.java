package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.ProfessionEntity;
import com.quick.recording.company.service.repository.dto.ProfessionDtoRepository;
import com.quick.recording.company.service.service.local.ProfessionService;
import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceProfessionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionControllerImpl
        extends CacheableMainControllerAbstract<ProfessionDto, ProfessionEntity, ProfessionDtoRepository, ProfessionService>
        implements CompanyServiceProfessionApi {

    @Autowired
    public ProfessionControllerImpl(ProfessionService service, ProfessionDtoRepository repository) {
        super(service, repository);
    }

}