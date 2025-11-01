package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.service.local.*;
import com.quick.recording.company.service.service.remote.RemoteUserService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.BaseDto;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.auth.AuthUserDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.dto.company.ProfessionDto;
import com.quick.recording.gateway.dto.company.ServiceDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Test Api Employee in company service")
public class EmployeeTestController extends MainTestController<EmployeeDto> {

    private TypeReference<EmployeeDto> typeDto = new TypeReference<EmployeeDto>() {};
    private TypeReference<Page<EmployeeDto>> typePageDto = new TypeReference<Page<EmployeeDto>>() {};

    private final UUID presentUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UUID notFoundUUID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @MockitoBean
    private RemoteUserService remoteUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProfessionService professionService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ActivityService activityService;


    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(employeeService, professionService, serviceService, companyService, activityService);
    }

    @Override
    public String uri() {
        return "/api/v1/employee";
    }

    @BeforeEach
    void setUp() {
        ApiError apiError = new ApiError();

        AuthUserDto userDto = new AuthUserDto();
        userDto.setUuid(presentUUID);
        userDto.setUsername("Test");

        //User service
        when(remoteUserService.byUuid(presentUUID))
                .thenReturn(ResponseEntity.ok(userDto));

        when(remoteUserService.byUuid(notFoundUUID))
                .thenAnswer(invocationOnMock -> ResponseEntity.status(500).body(apiError));

        when(remoteUserService.getType())
                .thenReturn(AuthUserDto.class);

    }

    @Override
    public List<TestCase<EmployeeDto, ?>> postGetTestCases() {
        BaseDto emptyDto = new BaseDto();

        CompanyDto companyDto = getDtoFromService(companyService);
        if(Objects.isNull(companyDto)) {
            companyDto = CompanyServiceAppFactory
                    .createCompany(companyService, CompanyServiceAppFactory.createActivity(activityService));
        }

        ServiceDto serviceDto = getDtoFromService(serviceService);
        if(Objects.isNull(serviceDto)) {
            serviceDto = CompanyServiceAppFactory.createService(serviceService, companyDto);
        }

        ProfessionDto professionDto = getDtoFromService(professionService);
        if(Objects.isNull(professionDto)) {
            professionDto = CompanyServiceAppFactory.createProfession(professionService, companyDto, serviceDto);
        }

        EmployeeDto fail1 = new EmployeeDto();
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(5));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("authId"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("profession"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("permissions"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        EmployeeDto fail2 = new EmployeeDto();
        fail2.setProfession(new ProfessionDto());
        fail2.setServices(List.of(emptyDto));
        fail2.setCompany(emptyDto);
        fail2.setAuthId(notFoundUUID);
        fail2.setPermissions(List.of(CompanyHierarchyEnum.OWNER));
        TestCase<EmployeeDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(4));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("profession"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("authId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        final UUID companyId = companyDto.getUuid();
        final UUID serviceId = serviceDto.getUuid();
        final UUID professionId = professionDto.getUuid();
        EmployeeDto success = new EmployeeDto();
        success.setAuthId(presentUUID);
        success.setPermissions(List.of(CompanyHierarchyEnum.OWNER,CompanyHierarchyEnum.MANAGER));
        success.setCompany(companyDto);
        success.setProfession(professionDto);
        success.setServices(List.of(serviceDto));
        TestCase<EmployeeDto, EmployeeDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCompany().getUuid()).isEqualTo(companyId));
        test3.addTest(result -> assertThat(result.getResult().getProfession().getUuid()).isEqualTo(professionId));
        test3.addTest(result -> assertThat(result.getResult().getPermissions().isEmpty()).isFalse());
        test3.addTest(result -> assertThat(result.getResult().getPermissions().size()).isEqualTo(2));
        test3.addTest(result -> assertThat(result.getResult().getServices().isEmpty()).isFalse());
        test3.addTest(result -> assertThat(result.getResult().getServices()
                .stream()
                .anyMatch(item -> item.getUuid().equals(serviceId))
        ).isTrue());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        EmployeeDto fail1 = new EmployeeDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("EmployeeEntity")).isTrue());

        EmployeeDto success = this.getLastCreateObjectClone();
        TestCase<EmployeeDto, EmployeeDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getPermissions().size()).isEqualTo(success.getPermissions().size()));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> deleteHardGetTestCases() {
        EmployeeDto fail1 = new EmployeeDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("EmployeeEntity")).isTrue());

        EmployeeDto fail2 = new EmployeeDto();
        fail2.setUuid(null);
        TestCase<EmployeeDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        EmployeeDto success = this.getLastCreateObjectClone();
        TestCase<EmployeeDto, EmployeeDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> byUUIDAfterDeleteGetTestCases() {
        EmployeeDto fail1 = this.getLastDeletedObjectClone();
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("EmployeeEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> listGetTestCases() {
        EmployeeDto successAll = new EmployeeDto();
        TestCase<EmployeeDto, Page<EmployeeDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        EmployeeDto success = this.getLastCreateObjectClone();
        UUID authId = success.getAuthId();

        EmployeeDto successOne = new EmployeeDto();
        successOne.setAuthId(authId);
        TestCase<EmployeeDto, Page<EmployeeDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getAuthId()).isEqualTo(authId));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> putGetTestCases() {
        BaseDto emptyDto = new BaseDto();

        EmployeeDto fail1 = new EmployeeDto();
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(6));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("authId"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("profession"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("permissions"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        EmployeeDto fail2 = this.getLastCreateObjectClone();
        fail2.setCompany(emptyDto);
        fail2.setProfession(new ProfessionDto());
        fail2.setServices(List.of(emptyDto));
        fail2.setAuthId(notFoundUUID);
        fail2.setPermissions(List.of(CompanyHierarchyEnum.OWNER,CompanyHierarchyEnum.MANAGER));
        TestCase<EmployeeDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(4));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("authId"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("profession"))
        ).isTrue());

        ServiceDto service = CompanyServiceAppFactory.createService(serviceService, getDtoFromService(companyService));

        EmployeeDto success1 = this.getLastCreateObjectClone();
        success1.setPermissions(List.of(CompanyHierarchyEnum.OWNER));
        success1.getServices().add(service);
        int sizeAdd = success1.getServices().size();
        TestCase<EmployeeDto, EmployeeDto> test3 = new TestCase<>(success1, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success1.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getPermissions().size()).isEqualTo(1));
        test3.addTest(result -> assertThat(result.getResult().getPermissions().get(0)).isEqualTo(CompanyHierarchyEnum.OWNER));
        test3.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(sizeAdd));

        EmployeeDto success2 = this.getLastCreateObjectClone();
        success2.setPermissions(List.of(CompanyHierarchyEnum.OWNER, CompanyHierarchyEnum.MANAGER));
        success2.getServices().remove(service);
        int sizeRemove = success2.getServices().size();
        TestCase<EmployeeDto, EmployeeDto> test4 = new TestCase<>(success2, typeDto);
        test4.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test4.addTest(result -> assertThat(result.getResult()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test4.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success2.getUuid()));
        test4.addTest(result -> assertThat(result.getResult().getPermissions().size()).isEqualTo(2));
        test4.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(sizeRemove));

        return List.of(test1, test2, test3, test4);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> patchGetTestCases() {
        EmployeeDto create = this.getLastCreateObjectClone();
        BaseDto emptyDto = new BaseDto();

        EmployeeDto fail1 = new EmployeeDto();
        fail1.setCompany(emptyDto);
        fail1.setProfession(new ProfessionDto());
        fail1.setServices(List.of(emptyDto));
        fail1.setAuthId(notFoundUUID);
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(5));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("uuid"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("authId"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("profession"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("services"))
        ).isTrue());

        int serviceSize = create.getServices().size() + 1;
        int permissionSize = create.getPermissions().size() + 1;
        ServiceDto service = CompanyServiceAppFactory.createService(serviceService, getDtoFromService(companyService));
        EmployeeDto success1 = new EmployeeDto();
        success1.setUuid(create.getUuid());
        success1.setIsActive(false);
        success1.setServices(List.of(service));
        success1.setPermissions(List.of(CompanyHierarchyEnum.ASSISTANT));
        TestCase<EmployeeDto, EmployeeDto> test2 = new TestCase<>(success1, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getServices().size()).isEqualTo(serviceSize));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getPermissions().size()).isEqualTo(permissionSize));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> deleteSoftGetTestCases() {
        EmployeeDto fail1 = new EmployeeDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("EmployeeEntity")).isTrue());

        EmployeeDto fail2 = new EmployeeDto();
        fail2.setUuid(null);
        TestCase<EmployeeDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        EmployeeDto success = this.getLastCreateObjectClone();
        TestCase<EmployeeDto, EmployeeDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<EmployeeDto, ?>> restoreGetTestCases() {
        EmployeeDto fail1 = new EmployeeDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<EmployeeDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("EmployeeEntity")).isTrue());

        EmployeeDto fail2 = new EmployeeDto();
        fail2.setUuid(null);
        TestCase<EmployeeDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        EmployeeDto success = this.getLastCreateObjectClone();
        TestCase<EmployeeDto, EmployeeDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }

}
