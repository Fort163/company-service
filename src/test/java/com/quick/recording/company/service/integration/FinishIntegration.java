package com.quick.recording.company.service.integration;

import com.quick.recording.company.service.service.local.*;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Finish integration test company service")
public class FinishIntegration {

    @LocalServerPort
    private int port;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GeocoderService geocoderService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProfessionService professionService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Order(1)
    void loadContext() {
        assertThat(activityService).isNotNull();
        assertThat(geocoderService).isNotNull();
        assertThat(companyService).isNotNull();
        assertThat(serviceService).isNotNull();
        assertThat(professionService).isNotNull();
        assertThat(scheduleService).isNotNull();
        assertThat(employeeService).isNotNull();
    }

    @Test
    @Order(2)
    void clearDataBase() {
        clearService(geocoderService);
        clearService(employeeService);
        clearService(professionService);
        clearService(serviceService);
        clearService(scheduleService);
        clearService(companyService);
        clearService(activityService);
        assertThat(geocoderService.findAll().isEmpty()).isTrue();
        assertThat(employeeService.findAll().isEmpty()).isTrue();
        assertThat(professionService.findAll().isEmpty()).isTrue();
        assertThat(serviceService.findAll().isEmpty()).isTrue();
        assertThat(scheduleService.findAll().isEmpty()).isTrue();
        assertThat(companyService.findAll().isEmpty()).isTrue();
        assertThat(activityService.findAll().isEmpty()).isTrue();
    }

    private void clearService(MainService<? extends SmartEntity, ? extends SmartDto> service){
        List<? extends SmartEntity> all = service.findAll();
        if (Objects.nonNull(all) && !all.isEmpty()) {
            service.deleteAll(all.stream().map(SmartEntity::getUuid).collect(Collectors.toList()));
        }
    }

}

