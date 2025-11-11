package com.quick.recording.company.service.service.local;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.company.service.mapper.CompanyMapper;
import com.quick.recording.company.service.repository.entity.CompanyEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
public class CompanyServiceImpl
        extends CacheableMainServiceAbstract<CompanyEntity, CompanyDto, CompanyEntityRepository, CompanyMapper>
        implements CompanyService {

    private final Map<Integer, Double> longitudeMap = new HashMap<>(){{
        put(0,111.3);
        put(10,109.6);
        put(20,104.6);
        put(30,96.4);
        put(40,85.3);
        put(50,78.7);
        put(60,71.5);
        put(70,55.7);
        put(80,38.0);
        put(90,0.0);
    }};
    private final Double latitudeRadius = 0.5;

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

    @Override
    protected Map<String, List<Predicate>> customPredicate(CriteriaBuilder criteriaBuilder, Root<CompanyEntity> root, CompanyEntity filter) {
        Map<String, List<Predicate>> result = new HashMap<>();
        List<Predicate> resultList = new ArrayList<>();
        GeocoderEntity geoPosition = filter.getGeoPosition();
        if(Objects.nonNull(geoPosition)) {
            Join<CompanyEntity, GeocoderEntity> join = root.join("geoPosition", JoinType.INNER);
            if (Objects.nonNull(geoPosition.getUuid())){
                resultList.add(criteriaBuilder.equal(join.get("uuid"), geoPosition.getUuid()));
            }
            if (Objects.nonNull(geoPosition.getName())){
                resultList.add(criteriaBuilder.like(
                        criteriaBuilder.lower(join.get("name")),
                        "%" + geoPosition.getName().trim().toLowerCase() + "%"
                ));
            }
            if (Objects.nonNull(geoPosition.getLatitude())){
                double from = geoPosition.getLatitude() - latitudeRadius;
                double to = geoPosition.getLatitude() + latitudeRadius;
                resultList.add(criteriaBuilder.between(join.get("latitude"), from, to));
            }
            if (Objects.nonNull(geoPosition.getLongitude())){
                double radius = getRadius(geoPosition.getLongitude());
                double from = geoPosition.getLongitude() - radius;
                double to = geoPosition.getLongitude() + radius;
                resultList.add(criteriaBuilder.between(join.get("longitude"), from, to));
            }
            result.put("geoPosition", resultList);
        }
        return result;
    }

    private double getRadius(Double longitude){
        int key = (int) Math.round(longitude / 10) * 10;
        Double handbookValue = longitudeMap.get(key);
        return (110.0 / handbookValue * 100) / 100 / 2;
    }

}
