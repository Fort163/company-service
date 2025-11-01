package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.ContextConstant;
import com.quick.recording.company.service.service.local.ActivityService;
import com.quick.recording.company.service.service.local.CompanyService;
import com.quick.recording.company.service.service.local.EmployeeService;
import com.quick.recording.company.service.service.local.ScheduleService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.DayOfWeek;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.quick.recording.company.service.ContextConstant.SCHEDULE_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Schedule in company service")
public class ScheduleTestController extends MainTestController<ScheduleDto> {

    private TypeReference<ScheduleDto> typeDto = new TypeReference<ScheduleDto>() {};
    private TypeReference<Page<ScheduleDto>> typePageDto = new TypeReference<Page<ScheduleDto>>() {};

    @Autowired
    private CompanyService companyService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(scheduleService, companyService, activityService);
    }

    @Override
    public String uri() {
        return "/api/v1/schedule";
    }

    @Override
    public String contextVariableName() {
        return SCHEDULE_DTO;
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> postGetTestCases() {
        CompanyDto companyDto;
        if(getTestContextHolder().isSuite()){
            companyDto = getTestContextHolder().getLast(ContextConstant.COMPANY_DTO);
        }
        else {
            companyDto = getDtoFromService(companyService);
            if(Objects.isNull(companyDto)) {
                companyDto = CompanyServiceAppFactory.createCompany(companyService,
                        CompanyServiceAppFactory.createActivity(activityService));
            }
        }

        final UUID companyUuid = companyDto.getUuid();

        ScheduleDto fail1 = new ScheduleDto();
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(5));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("clockFrom"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("clockTo"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("work"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("dayOfWeek"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        ScheduleDto fail2 = new ScheduleDto();
        BaseDto emptyDto = new BaseDto();
        fail2.setClockFrom(LocalTime.of(17,0));
        fail2.setClockTo(LocalTime.of(16,0));
        fail2.setWork(false);
        fail2.setDayOfWeek(DayOfWeek.monday);
        fail2.setCompany(emptyDto);
        fail2.setEmployee(emptyDto);
        TestCase<ScheduleDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("scheduleDto"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employee"))
        ).isTrue());

        ScheduleDto success1 = new ScheduleDto();
        success1.setClockFrom(LocalTime.of(10,0));
        success1.setClockTo(LocalTime.of(16,0));
        success1.setWork(true);
        success1.setDayOfWeek(DayOfWeek.thursday);
        success1.setCompany(companyDto);
        TestCase<ScheduleDto, ScheduleDto> test3 = new TestCase<>(success1, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getDayOfWeek()).isEqualTo(DayOfWeek.thursday));
        test3.addTest(result -> assertThat(result.getResult().getCompany().getUuid()).isEqualTo(companyUuid));

        ScheduleDto success2 = new ScheduleDto();
        success2.setClockFrom(LocalTime.of(10,0));
        success2.setClockTo(LocalTime.of(16,0));
        success2.setWork(false);
        success2.setDayOfWeek(DayOfWeek.monday);
        success2.setCompany(companyDto);
        TestCase<ScheduleDto, ScheduleDto> test4 = new TestCase<>(success2, typeDto);
        test4.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test4.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getDayOfWeek()).isEqualTo(DayOfWeek.monday));
        test4.addTest(result -> assertThat(result.getResult().getCompany().getUuid()).isEqualTo(companyUuid));

        return List.of(test1, test2, test3, test4);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        ScheduleDto fail1 = new ScheduleDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ScheduleEntity")).isTrue());

        ScheduleDto success = this.getLastCreateObjectClone();
        TestCase<ScheduleDto, ScheduleDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getDayOfWeek()).isEqualTo(DayOfWeek.monday));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> deleteHardGetTestCases() {
        ScheduleDto fail1 = new ScheduleDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ScheduleEntity")).isTrue());

        ScheduleDto fail2 = new ScheduleDto();
        fail2.setUuid(null);
        TestCase<ScheduleDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ScheduleDto success = this.getLastCreateObjectClone();
        TestCase<ScheduleDto, ScheduleDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> byUUIDAfterDeleteGetTestCases() {
        ScheduleDto fail1 = this.getLastDeletedObjectClone();
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ScheduleEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> listGetTestCases() {
        ScheduleDto successAll = new ScheduleDto();
        TestCase<ScheduleDto, Page<ScheduleDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        ScheduleDto successOne = new ScheduleDto();
        successOne.setDayOfWeek(DayOfWeek.monday);
        TestCase<ScheduleDto, Page<ScheduleDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getContent().stream()
                .anyMatch(item -> !item.getDayOfWeek().equals(DayOfWeek.monday))
        ).isFalse());

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> putGetTestCases() {
        BaseDto emptyDto = new BaseDto();

        ScheduleDto fail1 = new ScheduleDto();
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(6));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("clockFrom"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("clockTo"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("work"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("dayOfWeek"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        ScheduleDto fail2 = this.getLastCreateObjectClone();
        fail2.setCompany(emptyDto);
        fail2.setEmployee(emptyDto);
        TestCase<ScheduleDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employee"))
        ).isTrue());

        ScheduleDto success = this.getLastCreateObjectClone();
        success.setClockFrom(LocalTime.of(8,0));
        success.setClockTo(LocalTime.of(18,0));
        success.setWork(true);
        success.setDayOfWeek(DayOfWeek.thursday);
        TestCase<ScheduleDto, ScheduleDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getDayOfWeek()).isEqualTo(DayOfWeek.thursday));
        test3.addTest(result -> assertThat(result.getResult().getWork()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getClockFrom()).isEqualTo(LocalTime.of(8,0)));
        test3.addTest(result -> assertThat(result.getResult().getClockTo()).isEqualTo(LocalTime.of(18,0)));

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> patchGetTestCases() {
        ScheduleDto create = this.getLastCreateObjectClone();
        BaseDto emptyDto = new BaseDto();

        ScheduleDto fail1 = new ScheduleDto();
        fail1.setCompany(emptyDto);
        fail1.setEmployee(emptyDto);
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employee"))
        ).isTrue());

        ScheduleDto success1 = new ScheduleDto();
        success1.setUuid(create.getUuid());
        success1.setDayOfWeek(DayOfWeek.friday);
        success1.setClockFrom(LocalTime.of(9,0));
        success1.setIsActive(false);
        TestCase<ScheduleDto, ScheduleDto> test2 = new TestCase<>(success1, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getDayOfWeek()).isEqualTo((DayOfWeek.friday)));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getClockFrom()).isEqualTo(LocalTime.of(9,0)));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> deleteSoftGetTestCases() {
        ScheduleDto fail1 = new ScheduleDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ScheduleEntity")).isTrue());

        ScheduleDto fail2 = new ScheduleDto();
        fail2.setUuid(null);
        TestCase<ScheduleDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ScheduleDto success = this.getLastCreateObjectClone();
        TestCase<ScheduleDto, ScheduleDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ScheduleDto, ?>> restoreGetTestCases() {
        ScheduleDto fail1 = new ScheduleDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ScheduleDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ScheduleEntity")).isTrue());

        ScheduleDto fail2 = new ScheduleDto();
        fail2.setUuid(null);
        TestCase<ScheduleDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ScheduleDto success = this.getLastCreateObjectClone();
        TestCase<ScheduleDto, ScheduleDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
