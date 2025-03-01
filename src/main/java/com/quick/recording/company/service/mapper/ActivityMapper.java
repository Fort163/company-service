package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.mapper.MainMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends MainMapper<ActivityEntity, ActivityDto> {


}
