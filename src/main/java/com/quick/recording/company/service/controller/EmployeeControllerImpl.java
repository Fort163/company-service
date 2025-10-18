package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.EmployeeEntity;
import com.quick.recording.company.service.repository.dto.EmployeeDtoRepository;
import com.quick.recording.company.service.service.EmployeeService;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceEmployeeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeControllerImpl
        extends CacheableMainControllerAbstract<EmployeeDto, EmployeeEntity, EmployeeDtoRepository, EmployeeService>
        implements CompanyServiceEmployeeApi {

    @Autowired
    public EmployeeControllerImpl(EmployeeService service, EmployeeDtoRepository repository) {
        super(service, repository);
    }

}
