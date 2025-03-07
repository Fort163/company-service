package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.mapper.ActivityMapper;
import com.quick.recording.company.service.repository.ActivityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.SearchActivityDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final MessageUtil messageUtil;

    @Override
    @CircuitBreaker(name = "database")
    public ActivityDto byUuid(UUID uuid) {
        Assert.notNull(uuid, "Uuid cannot be null");
        ActivityEntity activityEntity = activityRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException(messageUtil, ActivityEntity.class, uuid)
        );
        return activityMapper.toDto(activityEntity);
    }

    @Override
    @CircuitBreaker(name = "database")
    public Page<ActivityDto> list(SearchActivityDto searchActivityDto, Pageable pageable) {
        List<ActivityEntity> list = activityRepository.searchActivity(searchActivityDto, pageable);
        long count = activityRepository.searchActivityCount(searchActivityDto);
        List<ActivityDto> result = activityMapper.toDtoList(list);
        return new PageImpl<ActivityDto>(result, pageable, count);
    }

    @Override
    @CircuitBreaker(name = "database")
    public ActivityDto post(ActivityDto activityDto) {
        ActivityEntity activityEntity = activityMapper.toEntity(activityDto);
        return activityMapper.toDto(activityRepository.save(activityEntity));
    }

    @Override
    @CircuitBreaker(name = "database")
    public ActivityDto patch(ActivityDto activity) {
        Assert.notNull(activity.getUuid(), "Uuid cannot be null");
        ActivityEntity activityEntity = activityRepository.findById(activity.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, ActivityEntity.class, activity.getUuid())
        );
        activityEntity = activityMapper.toEntityWithoutNull(activity, activityEntity);
        return activityMapper.toDto(activityRepository.save(activityEntity));
    }

    @Override
    @CircuitBreaker(name = "database")
    public ActivityDto put(ActivityDto activity) {
        Assert.notNull(activity.getUuid(), "Uuid cannot be null");
        ActivityEntity activityEntity = activityRepository.findById(activity.getUuid()).orElseThrow(
                () -> new NotFoundException(messageUtil, ActivityEntity.class, activity.getUuid())
        );
        activityEntity = activityMapper.toEntity(activity, activityEntity);
        return activityMapper.toDto(activityRepository.save(activityEntity));
    }

    @Override
    @CircuitBreaker(name = "database")
    public Boolean delete(UUID uuid) {
        Assert.notNull(uuid, "Uuid cannot be null");
        ActivityEntity activityEntity = activityRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException(messageUtil, ActivityEntity.class, uuid)
        );
        activityEntity.setIsActive(false);
        activityRepository.save(activityEntity);
        return true;
    }

}
