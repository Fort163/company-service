package com.quick.recording.company.service.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "company_entity")
public class CompanyEntity extends AuditEntity {

        @Column(name = "name")
        private String name;
        @Column(name = "is_active")
        private Boolean isActive = true;
        @OneToMany(mappedBy = "companyEntity")
        private List<ServiceTypeEntity> serviceTypeEntities;
        @OneToMany(mappedBy = "companyEntity")
        private List<GeoPositionEntity> geoPositionEntities;
        @ManyToMany
        @JoinTable(name = "company2activity",
                joinColumns = {@JoinColumn(name= "company_uuid", referencedColumnName = "uuid")},
        inverseJoinColumns = {@JoinColumn(name = "activity_uuid", referencedColumnName = "uuid")})
        private List<ActivityEntity> activityEntities;

}
