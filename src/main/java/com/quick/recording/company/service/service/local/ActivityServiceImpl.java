package com.quick.recording.company.service.service.local;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.mapper.ActivityMapper;
import com.quick.recording.company.service.repository.entity.ActivityEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl
        extends CacheableMainServiceAbstract<ActivityEntity, ActivityDto, ActivityEntityRepository, ActivityMapper>
        implements ActivityService {

    @Autowired
    public ActivityServiceImpl(ActivityEntityRepository repository, ActivityMapper mapper,
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
