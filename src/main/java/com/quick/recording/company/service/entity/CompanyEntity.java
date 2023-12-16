package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity extends AuditEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company2activity",
            joinColumns = {@JoinColumn(name = "company2activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "activity2company_id")}
    )
    private List<ActivityEntity> activity;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private GeocoderEntity geoPosition;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScheduleEntity> schedules;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ServiceEntity> service;

}
