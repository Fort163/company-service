package com.quick.recording.company.service.repository;

import com.quick.recording.company.service.entity.GeoObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GeoObjectRepository extends JpaRepository<GeoObjectEntity, UUID> {

}
