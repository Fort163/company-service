package com.quick.recording.company.service.service.local;

import com.quick.recording.company.service.entity.ScheduleEntity;
import com.quick.recording.company.service.mapper.ScheduleMapper;
import com.quick.recording.company.service.repository.entity.ScheduleEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl
        extends CacheableMainServiceAbstract<ScheduleEntity, ScheduleDto, ScheduleEntityRepository, ScheduleMapper>
        implements ScheduleService {

    public ScheduleServiceImpl(ScheduleEntityRepository repository,
                               ScheduleMapper mapper,
                               MessageUtil messageUtil,
                               StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, ScheduleEntity.class, streamBridge);
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        exampleMatcher = exampleMatcher.withIgnorePaths("clockFrom", "clockTo");
        return exampleMatcher;
    }

    @Override
    public Class<ScheduleDto> getType() {
        return ScheduleDto.class;
    }
}
