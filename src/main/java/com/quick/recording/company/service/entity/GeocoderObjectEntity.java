package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "geocoder_object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocoderObjectEntity extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="geocoder_id")
    private GeocoderEntity geocoder;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "kind",nullable = false)
    private String kind;

}
