package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Activity")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntity extends SmartEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

}
