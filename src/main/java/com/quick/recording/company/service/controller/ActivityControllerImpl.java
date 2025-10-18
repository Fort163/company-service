package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.repository.dto.ActivityDtoRepository;
import com.quick.recording.company.service.service.ActivityService;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.main.controller.CacheableMainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceActivityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityControllerImpl
        extends CacheableMainControllerAbstract<ActivityDto, ActivityEntity, ActivityDtoRepository, ActivityService>
        implements CompanyServiceActivityApi {

    @Autowired
    public ActivityControllerImpl(ActivityService activityService, ActivityDtoRepository repository) {
        super(activityService, repository);
    }

}
