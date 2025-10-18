package com.quick.recording.company.service.repository.dto;

import com.quick.recording.gateway.dto.company.ServiceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceDtoRepository extends JpaRepository<ServiceDto, UUID> {
}
