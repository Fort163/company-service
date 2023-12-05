package com.quick.recording.company.service.repository;


import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.gateway.dto.company.SearchActivityDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, UUID>, PagingAndSortingRepository<ActivityEntity, UUID> {

    @Query("SELECT activity FROM Activity activity " +
            "WHERE (UPPER(activity.name) LIKE UPPER(:#{'%' + #search.name + '%'}) OR :#{#search.name} IS NULL) " +
            " AND (UPPER(activity.description) LIKE UPPER(:#{'%' + #search.description + '%'}) OR :#{#search.description} IS NULL) " +
            " AND (activity.isActive =:#{#search.isActive} OR :#{#search.isActive} IS NULL ) ")
    List<ActivityEntity> searchActivity(@Param("search") SearchActivityDto searchActivityDto, Pageable pageable);

    @Query("SELECT count(activity.uuid) FROM Activity activity " +
            "WHERE (UPPER(activity.name) LIKE UPPER(:#{'%' + #search.name + '%'}) OR :#{#search.name} IS NULL) " +
            " AND (UPPER(activity.description) LIKE UPPER(:#{'%' + #search.description + '%'}) OR :#{#search.description} IS NULL) " +
            " AND (activity.isActive =:#{#search.isActive} OR :#{#search.isActive} IS NULL ) ")
    long searchUserCount(@Param("search") SearchActivityDto search);

}
