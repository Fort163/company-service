package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.ServiceEntity;
import com.quick.recording.company.service.repository.dto.ServiceDtoRepository;
import com.quick.recording.company.service.service.local.ServiceService;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceControllerImpl
        extends CacheableMainControllerAbstract<ServiceDto, ServiceEntity, ServiceDtoRepository, ServiceService>
        implements CompanyServiceServiceApi {

    @Autowired
    public ServiceControllerImpl(ServiceService service, ServiceDtoRepository repository) {
        super(service, repository);
    }

}
