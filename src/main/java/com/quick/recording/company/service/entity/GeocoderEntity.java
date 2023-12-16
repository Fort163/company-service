package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "geocoder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocoderEntity extends AuditEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="company_id")
    private CompanyEntity company;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "longitude",nullable = false)
    private Double longitude;

    @Column(name = "latitude",nullable = false)
    private Double latitude;

    @OneToMany(mappedBy = "geocoder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GeocoderObjectEntity> geoObjects;

}
