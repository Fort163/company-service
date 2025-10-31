package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Entity(name = "schedule")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"company","employee"})
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity extends SmartEntity {

    @Column(name = "clock_from")
    private LocalTime clockFrom;

    @Column(name = "clock_to")
    private LocalTime clockTo;

    @Column(name = "work")
    private Boolean work;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(targetEntity = CompanyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToOne(targetEntity = EmployeeEntity.class)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;


}
