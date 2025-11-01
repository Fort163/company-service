package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.ContextConstant;
import com.quick.recording.company.service.service.local.ActivityService;
import com.quick.recording.company.service.service.local.CompanyService;
import com.quick.recording.company.service.service.local.ServiceService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.dto.yandex.GeocoderObjectDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Company in company service")
public class CompanyTestController extends MainTestController<CompanyDto> {

    private TypeReference<CompanyDto> typeDto = new TypeReference<CompanyDto>() {};
    private TypeReference<Page<CompanyDto>> typePageDto = new TypeReference<Page<CompanyDto>>() {};

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ServiceService serviceService;

    @Test
    @Order(1)
    public void loadContext() {
        assertThat(companyService).isNotNull();
        assertThat(activityService).isNotNull();
        assertThat(serviceService).isNotNull();
    }

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(serviceService,companyService,activityService);
    }

    @Override
    public String uri(){
        return "/api/v1/company";
    }

    @Override
    public String contextVariableName() {
        return ContextConstant.COMPANY_DTO;
    }

    @Override
    public List<TestCase<CompanyDto, ?>> postGetTestCases() {

        ActivityDto emptyActivity = new ActivityDto();

        GeocoderObjectDto geocoderObjectDto = new GeocoderObjectDto();
        geocoderObjectDto.setName("Россия");
        geocoderObjectDto.setKind("country");

        GeocoderDto geocoder1 = new GeocoderDto();
        geocoder1.setName("Россия тестовый адрес 1");
        geocoder1.setLatitude(50.1D);
        geocoder1.setLongitude(53.1D);
        geocoder1.setGeoObjects(List.of(geocoderObjectDto));

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setClockFrom(LocalTime.of(8,0));
        scheduleDto.setClockTo(LocalTime.of(16,0));
        scheduleDto.setDayOfWeek(DayOfWeek.monday);
        scheduleDto.setWork(true);
        List<ScheduleDto> scheduleList = List.of(scheduleDto);

        List<ActivityDto> activityList = getDtoListFromService(activityService);

        if(activityList.isEmpty()){
            CompanyServiceAppFactory.createActivity(activityService);
            activityList = getDtoListFromService(activityService);
        }

        int activitySize = activityList.size();

        CompanyDto dto1 = new CompanyDto();
        dto1.setName("Test 1");
        dto1.setActivities(activityList);
        dto1.setSchedules(scheduleList);
        TestCase<CompanyDto, CompanyDto> test1 = new TestCase<>(dto1, typeDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getGeoPosition()).isNull());
        test1.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(activitySize));
        test1.addTest(result -> assertThat(result.getResult().getSchedules().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test 1"));

        CompanyDto dto2 = new CompanyDto();
        dto2.setName("Test 2");
        dto2.setGeoPosition(geocoder1);
        dto2.setSchedules(scheduleList);
        TestCase<CompanyDto, CompanyDto> test2= new TestCase<>(dto2, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getGeoPosition().getUuid()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(0));
        test2.addTest(result -> assertThat(result.getResult().getSchedules().size()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test 2"));

        CompanyDto dto3 = new CompanyDto();
        dto3.setName("Test 3");
        dto3.setGeoPosition(geocoder1);
        dto3.setActivities(activityList);
        TestCase<CompanyDto, CompanyDto> test3 = new TestCase<>(dto3, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getGeoPosition().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(activitySize));
        test3.addTest(result -> assertThat(result.getResult().getSchedules().size()).isEqualTo(0));
        test3.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test 3"));

        CompanyDto dto4 = new CompanyDto();
        dto4.setName("Test 4");
        dto4.setGeoPosition(geocoder1);
        dto4.setActivities(List.of(emptyActivity));
        dto4.setSchedules(scheduleList);
        TestCase<CompanyDto, ApiError> test4 = new TestCase<>(dto4, typeError);
        test4.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test4.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test4.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("activities")).isTrue());

        CompanyDto dto5 = new CompanyDto();
        dto5.setName("Test 4");
        dto5.setGeoPosition(geocoder1);
        dto5.setActivities(activityList);
        dto5.setSchedules(scheduleList);
        TestCase<CompanyDto, CompanyDto> test5 = new TestCase<>(dto5, typeDto);
        test5.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test5.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test5.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test 4"));
        test5.addTest(result -> assertThat(result.getResult().getGeoPosition().getUuid()).isNotNull());
        test5.addTest(result -> assertThat(result.getResult().getSchedules().size()).isEqualTo(1));
        test5.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(activitySize));

        CompanyDto dto6 = new CompanyDto();
        dto6.setName(null);
        dto6.setGeoPosition(geocoder1);
        dto6.setSchedules(scheduleList);
        TestCase<CompanyDto, ApiError> test6 = new TestCase<>(dto6, typeError);
        test6.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test6.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test6.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("name")).isTrue());

        return List.of(test1, test2, test3, test4, test5, test6);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        CompanyDto fail1 = new CompanyDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        CompanyDto success = this.getLastCreateObjectClone();
        TestCase<CompanyDto, CompanyDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test 4"));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> deleteHardGetTestCases() {
        CompanyDto fail1 = new CompanyDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        CompanyDto fail2 = new CompanyDto();
        fail2.setUuid(null);
        TestCase<CompanyDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        CompanyDto success = this.getLastCreateObjectClone();
        TestCase<CompanyDto, CompanyDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> byUUIDAfterDeleteGetTestCases() {
        CompanyDto fail1 = this.getLastDeletedObjectClone();
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> listGetTestCases() {
        CompanyDto successAll = new CompanyDto();
        TestCase<CompanyDto, Page<CompanyDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        CompanyDto successOne = new CompanyDto();
        successOne.setName("Test 4");
        TestCase<CompanyDto, Page<CompanyDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getName()).isEqualTo("Test 4"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> putGetTestCases() {
        ActivityDto emptyActivity = new ActivityDto();
        CompanyDto company = this.getLastCreateObjectClone();

        List<ScheduleDto> schedules = company.getSchedules();
        List<ActivityDto> activities = company.getActivities();

        if(activities.isEmpty()){
            CompanyServiceAppFactory.createActivity(activityService);
        }
        List<ActivityDto> activityList = getDtoListFromService(activityService);
        int size = activityList.size();

        GeocoderDto geocoderNew = new GeocoderDto();
        geocoderNew.setName("Россия тестовый адрес 2");
        geocoderNew.setLatitude(50.1D);
        geocoderNew.setLongitude(53.1D);
        geocoderNew.setGeoObjects(List.of());

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setClockFrom(LocalTime.of(8,0));
        scheduleDto.setClockTo(LocalTime.of(16,0));
        scheduleDto.setDayOfWeek(DayOfWeek.friday);
        scheduleDto.setWork(true);

        CompanyDto fail1 = this.getLastCreateObjectClone();
        fail1.setGeoPosition(geocoderNew);
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result ->
                assertThat(
                        result.getResult().getErrors().stream().anyMatch(error -> error.contains("geoPosition"))
                ).isTrue()
        );

        CompanyDto fail2 = this.getLastCreateObjectClone();
        schedules.add(scheduleDto);
        fail2.setSchedules(schedules);
        TestCase<CompanyDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test2.addTest(result ->
                    assertThat(
                            result.getResult().getErrors().stream().anyMatch(error -> error.contains("schedules"))
                    ).isTrue());

        CompanyDto fail3 = this.getLastCreateObjectClone();
        activities.add(emptyActivity);
        fail3.setActivities(activities);
        TestCase<CompanyDto, ApiError> test3 = new TestCase<>(fail3, typeError);
        test3.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test3.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("activities")).isTrue());

        CompanyDto fail4 = this.getLastCreateObjectClone();
        fail4.setGeoPosition(null);
        TestCase<CompanyDto, ApiError> test4 = new TestCase<>(fail4, typeError);
        test4.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test4.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test4.addTest(result ->
                assertThat(
                        result.getResult().getErrors().stream().anyMatch(error -> error.contains("geoPosition"))
                ).isTrue()
        );

        CompanyDto fail5 = new CompanyDto();
        TestCase<CompanyDto, ApiError> test5 = new TestCase<>(fail5, typeError);
        test5.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test5.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(5));

        CompanyDto fail6 = this.getLastCreateObjectClone();
        fail6.setName(null);
        TestCase<CompanyDto, ApiError> test6 = new TestCase<>(fail6, typeError);
        test6.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test6.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test6.addTest(result ->
                assertThat(
                        result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
                ).isTrue()
        );

        CompanyDto fail7 = this.getLastCreateObjectClone();
        fail7.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test7 = new TestCase<>(fail7, typeError);
        test7.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test7.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());



        CompanyDto success1 = this.getLastCreateObjectClone();

        ServiceDto service = CompanyServiceAppFactory.createService(serviceService, success1);

        success1.setServices(List.of(service));
        success1.setDescription("Test description put new");
        success1.setActivities(activityList);
        TestCase<CompanyDto, CompanyDto> test8 = new TestCase<>(success1, typeDto);
        test8.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test8.addTest(result -> assertThat(result.getResult()).isNotNull());
        test8.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test8.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test8.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success1.getUuid()));
        test8.addTest(result -> assertThat(result.getResult().getDescription()).isEqualTo("Test description put new"));
        test8.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(size));
        test8.addTest(result -> assertThat(result.getResult().getServices().isEmpty()).isFalse());
        test8.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(1));

        List<ActivityDto> activitiesSingle = List.of(getDtoFromService(activityService));

        CompanyDto success2 = this.getLastCreateObjectClone();
        success2.setName("Test from put");
        if(Objects.nonNull(activitiesSingle)) {
            success2.setActivities(activitiesSingle);
        }
        success2.setServices(List.of());
        TestCase<CompanyDto, CompanyDto> test9 = new TestCase<>(success2, typeDto);
        test9.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test9.addTest(result -> assertThat(result.getResult()).isNotNull());
        test9.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test9.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test9.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success2.getUuid()));
        test9.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test from put"));
        if(Objects.nonNull(activitiesSingle)) {
            test9.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(1));
        }
        test9.addTest(result -> assertThat(result.getResult().getServices().isEmpty()).isTrue());

        return List.of(test1, test2, test3, test4, test5, test6, test7, test8, test9);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> patchGetTestCases() {
        CompanyDto create = this.getLastCreateObjectClone();
        int countActivity = create.getActivities().size();
        List<ActivityDto> activityList = getDtoListFromService(activityService);
        ActivityDto emptyActivity = new ActivityDto();
        ScheduleDto emptySchedule = new ScheduleDto();
        ServiceDto emptyService = new ServiceDto();

        List<UUID> listActivity = create.getActivities().stream().map(ActivityDto::getUuid).collect(Collectors.toList());

        Optional<ActivityDto> otherActivity = activityList.stream()
                .filter(item -> !listActivity.contains(item.getUuid()))
                .findFirst();

        CompanyDto fail1 = new CompanyDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        CompanyDto fail2 = new CompanyDto();
        fail2.setUuid(create.getUuid());
        fail2.setActivities(List.of(emptyActivity));
        TestCase<CompanyDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("activities")).isTrue());

        CompanyDto fail3 = new CompanyDto();
        fail3.setUuid(create.getUuid());
        fail3.setSchedules(List.of(emptySchedule));
        TestCase<CompanyDto, ApiError> test3 = new TestCase<>(fail3, typeError);
        test3.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test3.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("schedules")).isTrue());

        CompanyDto fail4 = new CompanyDto();
        fail4.setUuid(create.getUuid());
        fail4.setServices(List.of(emptyService));
        TestCase<CompanyDto, ApiError> test4 = new TestCase<>(fail4, typeError);
        test4.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test4.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test4.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("services")).isTrue());

        CompanyDto success1 = new CompanyDto();
        success1.setUuid(create.getUuid());
        success1.setName("Test from path");
        TestCase<CompanyDto, CompanyDto> test5 = new TestCase<>(success1, typeDto);
        test5.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test5.addTest(result -> assertThat(result.getResult()).isNotNull());
        test5.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test5.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test5.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success1.getUuid()));
        test5.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test from path"));
        test5.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(countActivity));

        CompanyDto success2 = new CompanyDto();
        success2.setUuid(create.getUuid());
        otherActivity.ifPresent(dto -> success2.setActivities(List.of(dto)));
        TestCase<CompanyDto, CompanyDto> test6 = new TestCase<>(success2, typeDto);
        test6.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test6.addTest(result -> assertThat(result.getResult()).isNotNull());
        test6.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test6.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test6.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success2.getUuid()));
        if(otherActivity.isPresent()) {
            test6.addTest(result -> assertThat(result.getResult().getActivities().size()).isEqualTo(countActivity + 1));
        }

        return List.of(test1, test2, test3, test4, test5, test6);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> deleteSoftGetTestCases() {
        CompanyDto fail1 = new CompanyDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        CompanyDto fail2 = new CompanyDto();
        fail2.setUuid(null);
        TestCase<CompanyDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        CompanyDto success = this.getLastCreateObjectClone();
        TestCase<CompanyDto, CompanyDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<CompanyDto, ?>> restoreGetTestCases() {
        CompanyDto fail1 = new CompanyDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<CompanyDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("CompanyEntity")).isTrue());

        CompanyDto fail2 = new CompanyDto();
        fail2.setUuid(null);
        TestCase<CompanyDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        CompanyDto success = this.getLastCreateObjectClone();
        TestCase<CompanyDto, CompanyDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
