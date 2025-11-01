package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.service.local.ActivityService;
import com.quick.recording.company.service.service.local.CompanyService;
import com.quick.recording.company.service.service.local.ServiceService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.entity.SmartEntity;
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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Service in company service")
public class ServiceTestController extends MainTestController<ServiceDto> {

    private TypeReference<ServiceDto> typeDto = new TypeReference<ServiceDto>() {};
    private TypeReference<Page<ServiceDto>> typePageDto = new TypeReference<Page<ServiceDto>>() {};

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ActivityService activityService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(serviceService, companyService, activityService);
    }

    @Override
    public String uri() {
        return "/api/v1/service";
    }

    @Override
    public List<TestCase<ServiceDto, ?>> postGetTestCases() {
        CompanyDto companyDto = getDtoFromService(companyService);
        if(Objects.isNull(companyDto)) {
            companyDto = CompanyServiceAppFactory.createCompany(companyService,
                    CompanyServiceAppFactory.createActivity(activityService));
        }

        ServiceDto fail1 = new ServiceDto();
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("workClock"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        ServiceDto fail2 = new ServiceDto();
        BaseDto emptyDto = new BaseDto();
        fail2.setName("Test");
        fail2.setWorkClock(LocalTime.of(3,1));
        fail2.setCompany(emptyDto);
        fail2.setEmployee(emptyDto);
        TestCase<ServiceDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employee"))
        ).isTrue());

        ServiceDto success = new ServiceDto();
        success.setName("Test");
        success.setWorkClock(LocalTime.of(3,1));
        success.setCompany(companyDto);
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCountPartTime()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCountPartTime() == 7).isTrue());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        ServiceDto fail1 = new ServiceDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ServiceEntity")).isTrue());

        ServiceDto success = this.getLastCreateObjectClone();
        TestCase<ServiceDto, ServiceDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> deleteHardGetTestCases() {
        ServiceDto fail1 = new ServiceDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ServiceEntity")).isTrue());

        ServiceDto fail2 = new ServiceDto();
        fail2.setUuid(null);
        TestCase<ServiceDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ServiceDto success = this.getLastCreateObjectClone();
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> byUUIDAfterDeleteGetTestCases() {
        ServiceDto fail1 = this.getLastDeletedObjectClone();
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ServiceEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> listGetTestCases() {
        ServiceDto successAll = new ServiceDto();
        TestCase<ServiceDto, Page<ServiceDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        ServiceDto successOne = new ServiceDto();
        successOne.setName("Test");
        TestCase<ServiceDto, Page<ServiceDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getName()).isEqualTo("Test"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> putGetTestCases() {
        BaseDto emptyDto = new BaseDto();

        ServiceDto fail1 = this.getLastCreateObjectClone();
        fail1.setName(null);
        fail1.setCompany(null);
        fail1.setWorkClock(null);
        fail1.setEmployee(null);
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("workClock"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        ServiceDto fail2 = this.getLastCreateObjectClone();
        fail2.setCompany(emptyDto);
        fail2.setEmployee(emptyDto);
        TestCase<ServiceDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("employee"))
        ).isTrue());

        ServiceDto success = this.getLastCreateObjectClone();
        success.setName("Test new");
        success.setWorkClock(LocalTime.of(4,10));
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test new"));
        test3.addTest(result -> assertThat(result.getResult().getCountPartTime() == 9).isTrue());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> patchGetTestCases() {
        ServiceDto create = this.getLastCreateObjectClone();
        BaseDto emptyDto = new BaseDto();

        ServiceDto fail1 = new ServiceDto();
        fail1.setCompany(emptyDto);
        fail1.setEmployee(emptyDto);
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
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

        ServiceDto success1 = new ServiceDto();
        success1.setUuid(create.getUuid());
        success1.setName("Test");
        success1.setWorkClock(LocalTime.of(1,0));
        TestCase<ServiceDto, ServiceDto> test2 = new TestCase<>(success1, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test2.addTest(result -> assertThat(result.getResult().getCountPartTime() == 2).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getWorkClock()).isEqualTo(LocalTime.of(1,0)));

        ServiceDto success2 = new ServiceDto();
        success2.setUuid(create.getUuid());
        success2.setName("Test new");
        success2.setWorkClock(LocalTime.of(3,25));
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success2, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test new"));
        test3.addTest(result -> assertThat(result.getResult().getCountPartTime() == 7).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getWorkClock()).isEqualTo(LocalTime.of(3,25)));

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> deleteSoftGetTestCases() {
        ServiceDto fail1 = new ServiceDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ServiceEntity")).isTrue());

        ServiceDto fail2 = new ServiceDto();
        fail2.setUuid(null);
        TestCase<ServiceDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ServiceDto success = this.getLastCreateObjectClone();
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ServiceDto, ?>> restoreGetTestCases() {
        ServiceDto fail1 = new ServiceDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ServiceDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ServiceEntity")).isTrue());

        ServiceDto fail2 = new ServiceDto();
        fail2.setUuid(null);
        TestCase<ServiceDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ServiceDto success = this.getLastCreateObjectClone();
        TestCase<ServiceDto, ServiceDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
