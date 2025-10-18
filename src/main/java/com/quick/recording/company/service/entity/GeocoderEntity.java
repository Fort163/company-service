package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "geocoder")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"company","geoObjects"})
@NoArgsConstructor
@AllArgsConstructor
public class GeocoderEntity extends SmartEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @OneToMany(mappedBy = "geocoder", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<GeocoderObjectEntity> geoObjects;

}
