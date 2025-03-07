package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity(name = "schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity extends AuditEntity {

    @Column(name = "clock_from")
    private LocalTime clockFrom;

    @Column(name = "clock_to")
    private LocalTime clockTo;

    @Column(name = "work")
    private Boolean work;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
