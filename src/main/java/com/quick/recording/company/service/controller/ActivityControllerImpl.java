package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.service.ActivityService;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.SearchActivityDto;
import com.quick.recording.gateway.service.company.CompanyServiceActivityApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityControllerImpl implements CompanyServiceActivityApi {

    private final ActivityService activityService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ActivityDto> byUuid(UUID uuid) {
        return ResponseEntity.ok(activityService.byUuid(uuid));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<ActivityDto> list(SearchActivityDto searchActivityDto, Pageable pageable) {
        return activityService.list(searchActivityDto, pageable);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE')")
    public ResponseEntity<ActivityDto> post(@RequestBody @Validated ActivityDto activityDto) {
        return ResponseEntity.ok(activityService.post(activityDto));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE')")
    public ResponseEntity<ActivityDto> patch(@RequestBody ActivityDto user) {
        return ResponseEntity.ok(activityService.patch(user));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE')")
    public ResponseEntity<ActivityDto> put(@RequestBody @Validated ActivityDto user) {
        return ResponseEntity.ok(activityService.put(user));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_WRITE')")
    public ResponseEntity<Boolean> delete(UUID uuid) {
        return ResponseEntity.ok(activityService.delete(uuid));
    }

}
