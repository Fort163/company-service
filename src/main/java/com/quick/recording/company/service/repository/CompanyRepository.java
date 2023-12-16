package com.quick.recording.company.service.repository;

import com.quick.recording.company.service.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID>, PagingAndSortingRepository<CompanyEntity, UUID> {

}
