package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.SearchActivityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityService {

    ActivityDto byUuid(UUID uuid);

    Page<ActivityDto> list(SearchActivityDto searchActivityDto, Pageable pageable);

    ActivityDto post(ActivityDto activityDto);

    ActivityDto patch(ActivityDto user);

    ActivityDto put(ActivityDto user);

    Boolean delete(UUID uuid);

}
