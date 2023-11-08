package com.quick.recording.company.service.entity;

import com.quick.recording.company.service.config.listener.AuditListener;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public class AuditEntity extends MainEntity {

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_when")
    private LocalDateTime createdWhen;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_when")
    private LocalDateTime updatedWhen;

}
