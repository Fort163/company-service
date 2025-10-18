package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {GeocoderObjectMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface GeocoderMapper extends MainMapper<GeocoderEntity, GeocoderDto> {
}
