package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.service.local.ActivityService;
import com.quick.recording.company.service.service.local.CompanyService;
import com.quick.recording.company.service.service.local.ProfessionService;
import com.quick.recording.company.service.service.local.ServiceService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Profession in company service")
public class ProfessionTestController extends MainTestController<ProfessionDto> {

    private TypeReference<ProfessionDto> typeDto = new TypeReference<ProfessionDto>() {};
    private TypeReference<Page<ProfessionDto>> typePageDto = new TypeReference<Page<ProfessionDto>>() {};

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProfessionService professionService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(professionService, serviceService, companyService, activityService);
    }

    @Override
    public String uri() {
        return "/api/v1/profession";
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> postGetTestCases() {
        CompanyDto companyDto = getDtoFromService(companyService);
        if(Objects.isNull(companyDto)) {
            companyDto = CompanyServiceAppFactory
                    .createCompany(companyService, CompanyServiceAppFactory.createActivity(activityService));
        }

        ServiceDto serviceDto = getDtoFromService(serviceService);
        if(Objects.isNull(serviceDto)) {
            serviceDto = CompanyServiceAppFactory.createService(serviceService, companyDto);
        }

        ProfessionDto fail1 = new ProfessionDto();
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(4));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("description"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        BaseDto emptyDto = new BaseDto();
        ProfessionDto fail2 = new ProfessionDto();
        fail2.setName("Test profession");
        fail2.setDescription("Test profession description");
        fail2.setCompany(emptyDto);
        fail2.setServices(List.of(emptyDto));
        TestCase<ProfessionDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        final UUID companyId = companyDto.getUuid();
        final UUID serviceId = serviceDto.getUuid();
        ProfessionDto success = new ProfessionDto();
        success.setName("Test profession");
        success.setDescription("Test profession description");
        success.setCompany(companyDto);
        success.setServices(List.of(serviceDto));
        TestCase<ProfessionDto, ProfessionDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany().getUuid()).isEqualTo(companyId));
        test3.addTest(result -> assertThat(result.getResult().getServices()
                .stream()
                .anyMatch(item -> item.getUuid().equals(serviceId))
        ).isTrue());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        ProfessionDto fail1 = new ProfessionDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ProfessionEntity")).isTrue());

        ProfessionDto success = this.getLastCreateObjectClone();
        TestCase<ProfessionDto, ProfessionDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test profession"));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> deleteHardGetTestCases() {
        ProfessionDto fail1 = new ProfessionDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ProfessionEntity")).isTrue());

        ProfessionDto fail2 = new ProfessionDto();
        fail2.setUuid(null);
        TestCase<ProfessionDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ProfessionDto success = this.getLastCreateObjectClone();
        TestCase<ProfessionDto, ProfessionDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> byUUIDAfterDeleteGetTestCases() {
        ProfessionDto fail1 = this.getLastDeletedObjectClone();
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ProfessionEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> listGetTestCases() {
        ProfessionDto successAll = new ProfessionDto();
        TestCase<ProfessionDto, Page<ProfessionDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        ProfessionDto successOne = new ProfessionDto();
        successOne.setName("Test profession");
        TestCase<ProfessionDto, Page<ProfessionDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getName()).isEqualTo("Test profession"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> putGetTestCases() {
        BaseDto emptyDto = new BaseDto();
        ProfessionDto create = this.getLastCreateObjectClone();

        ProfessionDto fail1 = this.getLastCreateObjectClone();
        fail1.setName(null);
        fail1.setDescription(null);
        fail1.setCompany(null);
        fail1.setServices(null);
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(4));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("description"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        ProfessionDto fail2 = this.getLastCreateObjectClone();
        fail2.setCompany(emptyDto);
        fail2.setServices(List.of(emptyDto));
        TestCase<ProfessionDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());


        ServiceDto serviceDto = CompanyServiceAppFactory.createService(serviceService, getDtoFromService(companyService));
        ProfessionDto success1 = this.getLastCreateObjectClone();
        success1.setName("Test profession new");
        success1.setDescription("Test profession description new");
        success1.getServices().add(serviceDto);
        int size = success1.getServices().size();
        TestCase<ProfessionDto, ProfessionDto> test3 = new TestCase<>(success1, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success1.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test profession new"));
        test3.addTest(result -> assertThat(result.getResult().getDescription()).isEqualTo("Test profession description new"));
        test3.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(size));

        ProfessionDto success2 = this.getLastCreateObjectClone();
        success2.setName("Test profession");
        success2.setDescription("Test profession description");
        int sizeNew = success2.getServices().size();
        TestCase<ProfessionDto, ProfessionDto> test4 = new TestCase<>(success2, typeDto);
        test4.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test4.addTest(result -> assertThat(result.getResult()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success2.getUuid()));
        test4.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test profession"));
        test4.addTest(result -> assertThat(result.getResult().getDescription()).isEqualTo("Test profession description"));
        test4.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(sizeNew));

        return List.of(test1, test2, test3, test4);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> patchGetTestCases() {
        ProfessionDto create = this.getLastCreateObjectClone();
        int startSizeService = create.getServices().size();
        ServiceDto serviceDto1 = CompanyServiceAppFactory.createService(serviceService, getDtoFromService(companyService));
        ServiceDto serviceDto2 = CompanyServiceAppFactory.createService(serviceService, getDtoFromService(companyService));
        BaseDto emptyDto = new BaseDto();

        ProfessionDto fail1 = new ProfessionDto();
        fail1.setCompany(emptyDto);
        fail1.setServices(List.of(emptyDto));
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        ProfessionDto success1 = new ProfessionDto();
        success1.setUuid(create.getUuid());
        success1.setName("Test profession new");
        success1.setServices(List.of(serviceDto1, serviceDto2));
        int addSizeService = startSizeService + 2;
        TestCase<ProfessionDto, ProfessionDto> test2 = new TestCase<>(success1, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test profession new"));
        test2.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(addSizeService));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> deleteSoftGetTestCases() {
        ProfessionDto fail1 = new ProfessionDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ProfessionEntity")).isTrue());

        ProfessionDto fail2 = new ProfessionDto();
        fail2.setUuid(null);
        TestCase<ProfessionDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ProfessionDto success = this.getLastCreateObjectClone();
        TestCase<ProfessionDto, ProfessionDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ProfessionDto, ?>> restoreGetTestCases() {
        ProfessionDto fail1 = new ProfessionDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ProfessionDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ProfessionEntity")).isTrue());

        ProfessionDto fail2 = new ProfessionDto();
        fail2.setUuid(null);
        TestCase<ProfessionDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ProfessionDto success = this.getLastCreateObjectClone();
        TestCase<ProfessionDto, ProfessionDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
