package com.quick.recording.company.service.service;

import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.CompanyDto;

import java.util.List;
import java.util.UUID;

public interface ActivityService {
    List<ActivityDto> getAll();

    void delete(UUID uuid);

    ActivityDto getById(UUID uuid);

    ActivityDto save(ActivityDto activityDto);

    ActivityDto update(UUID uuid,ActivityDto activityDto);
}
