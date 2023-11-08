package com.quick.recording.company.service.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "activity_entity")
public class ActivityEntity extends AuditEntity {
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "activityEntities")
    private List<CompanyEntity> companyEntities;
}
