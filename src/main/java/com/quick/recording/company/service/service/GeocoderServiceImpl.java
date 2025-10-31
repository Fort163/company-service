package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.company.service.entity.GeocoderObjectEntity;
import com.quick.recording.company.service.mapper.GeocoderMapper;
import com.quick.recording.company.service.repository.entity.GeocoderEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.dto.yandex.GeocoderObjectDto;
import com.quick.recording.gateway.main.service.local.MainServiceAbstract;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GeocoderServiceImpl
        extends MainServiceAbstract<GeocoderEntity, GeocoderDto, GeocoderEntityRepository, GeocoderMapper>
        implements GeocoderService {

    public GeocoderServiceImpl(GeocoderEntityRepository repository, GeocoderMapper mapper, MessageUtil messageUtil) {
        super(repository, mapper, messageUtil, GeocoderEntity.class);
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void beforePost(GeocoderEntity entity, GeocoderDto dto) {
        if (Objects.nonNull(entity.getGeoObjects())) {
            entity.getGeoObjects().forEach(item -> item.setGeocoder(entity));
        }
    }

    @Override
    protected void beforePut(GeocoderEntity entity, GeocoderDto dto) {
        List<UUID> idGeoObject = dto.getGeoObjects().stream()
                .map(GeocoderObjectDto::getUuid)
                .collect(Collectors.toList());
        List<GeocoderObjectEntity> removeGeoObject = entity.getGeoObjects().stream()
                .filter(item -> !idGeoObject.contains(item.getUuid()))
                .collect(Collectors.toList());
        for (GeocoderObjectEntity geoObject : removeGeoObject) {
            entity.getGeoObjects().remove(geoObject);
        }
        entity = getRepository().saveAndFlush(entity);
    }



    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

    @Override
    public Class<GeocoderDto> getType() {
        return GeocoderDto.class;
    }
}
