package com.quick.recording.company.service.integration;

import com.quick.recording.company.service.entity.ActivityEntity;
import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.company.service.main.TestContextHolder;
import com.quick.recording.company.service.service.ActivityService;
import com.quick.recording.company.service.service.CompanyService;
import com.quick.recording.company.service.service.GeocoderService;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.enumerated.Delete;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Finish integration test company service")
public class FinishIntegration {

    @LocalServerPort
    private int port;

    @Autowired
    private TestContextHolder testContextHolder;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GeocoderService geocoderService;

    @Autowired
    private CompanyService companyService;

    @Test
    void loadContext() {
        assertThat(testContextHolder).isNotNull();
        assertThat(activityService).isNotNull();
        assertThat(geocoderService).isNotNull();
        assertThat(companyService).isNotNull();

    }

    @Test
    @Order(1)
    void clearGeocoder() {
        List<GeocoderEntity> all = geocoderService.findAll();
        for (GeocoderEntity entity : all){
            GeocoderDto delete = geocoderService.delete(entity.getUuid(), Delete.HARD);
            assertThrows(NotFoundException.class, () -> geocoderService.byUuidEntity(delete.getUuid()));
        }
    }

    @Test
    @Order(5)
    void clearCompany() {
        List<CompanyEntity> all = companyService.findAll();
        for (CompanyEntity entity : all){
            CompanyDto delete = companyService.delete(entity.getUuid(), Delete.HARD);
            assertThrows(NotFoundException.class, () -> companyService.byUuidEntity(delete.getUuid()));
        }
    }

    @Test
    @Order(6)
    void clearActivity() {
        List<ActivityEntity> all = activityService.findAll();
        for(ActivityEntity entity : all) {
            ActivityDto delete = activityService.delete(entity.getUuid(), Delete.HARD);
            assertThrows(NotFoundException.class, () -> activityService.byUuidEntity(delete.getUuid()));
        }
    }

}

