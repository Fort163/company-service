package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "company")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"geoPosition","schedules","services"})
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity extends SmartEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "company2activity",
            joinColumns = {@JoinColumn(name = "company2activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "activity2company_id")}
    )
    private List<ActivityEntity> activities;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private GeocoderEntity geoPosition;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleEntity> schedules;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ServiceEntity> services;

}
