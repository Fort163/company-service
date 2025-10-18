package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Profession")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"company","service"})
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionEntity extends SmartEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "profession2service",
            joinColumns = {@JoinColumn(name = "profession2service_id")},
            inverseJoinColumns = {@JoinColumn(name = "service2profession_id")}
    )
    private List<ServiceEntity> services;

}
