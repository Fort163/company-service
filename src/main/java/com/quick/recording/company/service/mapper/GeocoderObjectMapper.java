package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.GeocoderObjectEntity;
import com.quick.recording.gateway.dto.yandex.GeocoderObjectDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface GeocoderObjectMapper extends MainMapper<GeocoderObjectEntity, GeocoderObjectDto> {
}
