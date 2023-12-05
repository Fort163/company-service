package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntity extends AuditEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private Boolean isActive = true;

}
