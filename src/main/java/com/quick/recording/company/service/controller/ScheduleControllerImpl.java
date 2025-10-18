package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.ScheduleEntity;
import com.quick.recording.company.service.repository.dto.ScheduleDtoRepository;
import com.quick.recording.company.service.service.ScheduleService;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceScheduleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleControllerImpl
        extends CacheableMainControllerAbstract<ScheduleDto, ScheduleEntity, ScheduleDtoRepository, ScheduleService>
        implements CompanyServiceScheduleApi {

    @Autowired
    public ScheduleControllerImpl(ScheduleService service, ScheduleDtoRepository repository) {
        super(service, repository);
    }

}

