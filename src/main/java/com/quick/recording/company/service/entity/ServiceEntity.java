package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity extends AuditEntity {

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "work_clock",nullable = false)
    private LocalTime workClock;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive;

    @Column(name = "count_part_time",nullable = false)
    private Byte countPartTime;

    @ManyToOne(optional=false)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
