package com.quick.recording.company.service.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "service_type_entity")
public class ServiceTypeEntity  extends AuditEntity{
    @Column(name = "count_part_time")
    private Integer countPartTime;
    @Column(name = "name")
    private String name;
    @Column(name = "show_client")
    private Boolean showClient;
    @Column(name = "work_clock")
    private Boolean workClock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "uuid")
    private CompanyEntity companyEntity;
}
