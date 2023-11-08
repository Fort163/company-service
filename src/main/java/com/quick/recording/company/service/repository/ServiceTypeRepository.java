package com.quick.recording.company.service.repository;

import com.quick.recording.company.service.entity.ServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity, UUID> {
}
