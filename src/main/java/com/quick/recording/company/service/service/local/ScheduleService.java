package com.quick.recording.company.service.service.local;

import com.quick.recording.company.service.entity.ScheduleEntity;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.main.service.local.MainService;

public interface ScheduleService extends MainService<ScheduleEntity, ScheduleDto> {
}
