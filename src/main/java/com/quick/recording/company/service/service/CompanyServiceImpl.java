package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.company.service.mapper.CompanyMapper;
import com.quick.recording.company.service.repository.entity.CompanyEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Service
public class CompanyServiceImpl
        extends CacheableMainServiceAbstract<CompanyEntity, CompanyDto, CompanyEntityRepository, CompanyMapper>
        implements CompanyService {

    @Autowired
    public CompanyServiceImpl(CompanyEntityRepository repository,
                              CompanyMapper mapper,
                              MessageUtil messageUtil,
                              StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, CompanyEntity.class, streamBridge);
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void beforePost(CompanyEntity entity, CompanyDto dto) {
        GeocoderEntity geoPosition = entity.getGeoPosition();
        if (Objects.nonNull(geoPosition)) {
            geoPosition.setCompany(entity);
            geoPosition.getGeoObjects().forEach(item -> item.setGeocoder(geoPosition));
        }
        if (Objects.nonNull(entity.getSchedules()))
            entity.getSchedules().forEach(item -> item.setCompany(entity));
        else
            entity.setSchedules(Collections.emptyList());
        if (Objects.nonNull(entity.getServices()))
            entity.getServices().forEach(item -> item.setCompany(entity));
        else
            entity.setServices(Collections.emptyList());
        if (Objects.isNull(entity.getActivities()))
            entity.setActivities(Collections.emptyList());
    }

    @Override
    public Class<CompanyDto> getType() {
        return CompanyDto.class;
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }
}
