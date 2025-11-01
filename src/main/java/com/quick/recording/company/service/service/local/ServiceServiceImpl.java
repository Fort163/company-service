package com.quick.recording.company.service.service.local;

import com.quick.recording.company.service.entity.ServiceEntity;
import com.quick.recording.company.service.mapper.ServiceMapper;
import com.quick.recording.company.service.repository.entity.ServiceEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import com.quick.recording.gateway.util.LocalTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceServiceImpl
        extends CacheableMainServiceAbstract<ServiceEntity, ServiceDto, ServiceEntityRepository, ServiceMapper>
        implements ServiceService{

    @Autowired
    public ServiceServiceImpl(ServiceEntityRepository repository,
                              ServiceMapper mapper,
                              MessageUtil messageUtil,
                              StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, ServiceEntity.class, streamBridge);
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void beforePost(ServiceEntity entity, ServiceDto dto) {
        entity.setCountPartTime(LocalTimeUtil.getPartTime(entity.getWorkClock()));
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void beforePatch(ServiceEntity entity, ServiceDto dto) {
        dto.setCountPartTime(LocalTimeUtil.getPartTime(dto.getWorkClock()));
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void beforePut(ServiceEntity entity, ServiceDto dto) {
        dto.setCountPartTime(LocalTimeUtil.getPartTime(dto.getWorkClock()));
    }

    @Override
    public Class<ServiceDto> getType() {
        return ServiceDto.class;
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

}
