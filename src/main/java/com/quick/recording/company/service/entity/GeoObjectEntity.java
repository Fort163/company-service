package com.quick.recording.company.service.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "geo_object_entity")
public class GeoObjectEntity  extends AuditEntity{
    @Column(name = "create_date")
    private LocalDate createDate;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "geo_position_id", referencedColumnName = "uuid")
    private GeoPositionEntity geoPositionEntity;
}
