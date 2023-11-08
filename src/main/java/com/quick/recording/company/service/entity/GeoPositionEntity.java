package com.quick.recording.company.service.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "geo_position_entity")
public class GeoPositionEntity  extends AuditEntity{
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "kind")
    private String kind;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "geoPositionEntity")
    private List<GeoObjectEntity> geoObjectEntities;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "uuid")
    private CompanyEntity companyEntity;

}
