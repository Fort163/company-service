package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.service.ActivityService;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.main.controller.MainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceActivityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityControllerImpl extends MainControllerAbstract<ActivityDto, ActivityEntity, ActivityService>
        implements CompanyServiceActivityApi {

    @Autowired
    public ActivityControllerImpl(ActivityService activityService) {
        super(activityService);
    }

    @Override
    public Class<ActivityDto> getType() {
        return ActivityDto.class;
    }

}
