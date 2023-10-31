package com.quick.recording.company.service.controller;

import com.quick.recording.gateway.dto.company.ScheduleDto;
import com.quick.recording.gateway.service.company.ScheduleController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule")
public class ScheduleControllerImpl implements ScheduleController {

    @Override
    @GetMapping({"/schedule/{uuid}"})
    public ResponseEntity<ScheduleDto> scheduleByCompanyUuid(String uuid) {
        if(uuid != null && !uuid.isEmpty()){
            ScheduleDto scheduleDto = new ScheduleDto();
            scheduleDto.setUuid(UUID.randomUUID());
            scheduleDto.setFromTime("с 10 ");
            scheduleDto.setToTime("до 19 видимо ");
            return ResponseEntity.ok(scheduleDto);
        }
        return ResponseEntity.notFound().build();
    }

}

