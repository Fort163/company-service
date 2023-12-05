package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.gateway.dto.company.ActivityDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityEntity toActivityEntity(ActivityDto activityDto);

    ActivityDto toActivityDto(ActivityEntity entity);

    List<ActivityDto> toActivityDtoList(List<ActivityEntity> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActivityEntity toActivityEntityWithNull(ActivityDto dto, @MappingTarget ActivityEntity entity);


    ActivityEntity toActivityEntity(ActivityDto dto, @MappingTarget ActivityEntity entity);

}
