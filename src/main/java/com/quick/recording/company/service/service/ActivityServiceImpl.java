package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.mapper.ActivityMapper;
import com.quick.recording.company.service.repository.ActivityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import com.quick.recording.gateway.main.service.local.MainServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl extends CacheableMainServiceAbstract<ActivityEntity, ActivityDto> implements ActivityService {

    @Autowired
    public ActivityServiceImpl(ActivityRepository repository, ActivityMapper mapper,
                               MessageUtil messageUtil, StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, ActivityEntity.class, streamBridge);
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

    @Override
    public Class<ActivityDto> getType() {
        return ActivityDto.class;
    }
}
