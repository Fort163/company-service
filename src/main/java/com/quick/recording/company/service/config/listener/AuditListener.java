package com.quick.recording.company.service.config.listener;

import com.quick.recording.company.service.entity.AuditEntity;
import org.springframework.stereotype.Component;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AuditListener {

    @PrePersist
    private void prePersist(AuditEntity entity){
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        entity.setCreatedWhen(LocalDateTime.now());
        entity.setUpdatedWhen(LocalDateTime.now());
        if(Objects.nonNull(authentication)){
            entity.setCreatedBy(authentication.getName());
            entity.setUpdatedBy(authentication.getName());
        }*/
    }

    @PreUpdate
    private void preUpdate(AuditEntity entity){
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        entity.setUpdatedWhen(LocalDateTime.now());
        if(Objects.nonNull(authentication)){
            entity.setUpdatedBy(authentication.getName());
        }*/
    }

}
