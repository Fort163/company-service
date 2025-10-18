package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.ProfessionEntity;
import com.quick.recording.company.service.mapper.ProfessionMapper;
import com.quick.recording.company.service.repository.entity.ProfessionEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ProfessionServiceImpl
        extends CacheableMainServiceAbstract<ProfessionEntity, ProfessionDto, ProfessionEntityRepository, ProfessionMapper>
        implements ProfessionService {

    public ProfessionServiceImpl(ProfessionEntityRepository repository,
                                 ProfessionMapper mapper,
                                 MessageUtil messageUtil,
                                 StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, ProfessionEntity.class, streamBridge);
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

    @Override
    public Class<ProfessionDto> getType() {
        return ProfessionDto.class;
    }
}
