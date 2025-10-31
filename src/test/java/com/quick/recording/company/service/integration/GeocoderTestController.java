package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.CompanyServiceAppFactory;
import com.quick.recording.company.service.service.ActivityService;
import com.quick.recording.company.service.service.CompanyService;
import com.quick.recording.company.service.service.GeocoderService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.dto.yandex.GeocoderObjectDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

import static com.quick.recording.company.service.ContextConstant.GEOCODER_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Geocoder in company service")
public class GeocoderTestController extends MainTestController<GeocoderDto> {

    private TypeReference<GeocoderDto> typeDto = new TypeReference<GeocoderDto>() {};
    private TypeReference<Page<GeocoderDto>> typePageDto = new TypeReference<Page<GeocoderDto>>() {};

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private GeocoderService geocoderService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(geocoderService, companyService, activityService);
    }

    @Override
    public String uri() {
        return "/api/v1/geocoder";
    }

    @Override
    public String contextVariableName() {
        return GEOCODER_DTO;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> postGetTestCases() {
        CompanyDto companyDto = CompanyServiceAppFactory.createCompany(companyService, getDtoFromService(activityService));
        GeocoderObjectDto go1 = new GeocoderObjectDto();
        go1.setName("Россия");
        go1.setKind("country");
        GeocoderObjectDto go2 = new GeocoderObjectDto();
        go2.setName("посёлок Придорожный");
        go2.setKind("locality");
        GeocoderObjectDto go3 = new GeocoderObjectDto();
        go3.setName("микрорайон Южный Город");
        go3.setKind("district");
        GeocoderObjectDto go4 = new GeocoderObjectDto();
        go4.setName("Лета улица");
        go4.setKind("street");

        GeocoderDto fail1 = new GeocoderDto();
        fail1.setCompany(companyDto);
        fail1.setGeoObjects(List.of(go1,go2,go3,go4));
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("longitude"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("latitude"))
        ).isTrue());

        GeocoderDto fail2 = new GeocoderDto();
        fail2.setLatitude(50.0);
        fail2.setLongitude(50.0);
        fail2.setName("Самарская область, Волжский район, квартал Южный Город-1, Лета, 13");
        TestCase<GeocoderDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(2));
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());
        test2.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("geoObjects"))
        ).isTrue());

        GeocoderDto success = new GeocoderDto();
        success.setLatitude(50.0);
        success.setLongitude(50.0);
        success.setName("Самарская область, Волжский район, квартал Южный Город-1, Лета, 13");
        success.setCompany(companyDto);
        success.setGeoObjects(List.of(go1,go2,go3,go4));
        TestCase<GeocoderDto, GeocoderDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        GeocoderDto fail1 = new GeocoderDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("GeocoderEntity")).isTrue());

        GeocoderDto success = this.getLastCreateObjectClone();
        TestCase<GeocoderDto, GeocoderDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Самарская область, Волжский район, " +
                "квартал Южный Город-1, Лета, 13"));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> deleteHardGetTestCases() {
        GeocoderDto fail1 = new GeocoderDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("GeocoderEntity")).isTrue());

        GeocoderDto fail2 = new GeocoderDto();
        fail2.setUuid(null);
        TestCase<GeocoderDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        GeocoderDto success = this.getLastCreateObjectClone();
        TestCase<GeocoderDto, GeocoderDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> byUUIDAfterDeleteGetTestCases() {
        GeocoderDto fail1 = this.getLastDeletedObjectClone();
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("GeocoderEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> listGetTestCases() {
        GeocoderDto successAll = new GeocoderDto();
        TestCase<GeocoderDto, Page<GeocoderDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        GeocoderDto successOne = new GeocoderDto();
        successOne.setName("Самарская область, Волжский район, квартал Южный Город-1, Лета, 13");
        TestCase<GeocoderDto, Page<GeocoderDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getName())
                .isEqualTo("Самарская область, Волжский район, квартал Южный Город-1, Лета, 13"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> putGetTestCases() {
        GeocoderDto fail1 = this.getLastCreateObjectClone();

        GeocoderObjectDto go = new GeocoderObjectDto();
        go.setName("микрорайон Южный Город");
        go.setKind("district");
        fail1.getGeoObjects().add(go);
        fail1.setName(null);
        fail1.setCompany(null);

        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(3));
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("name"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("geoObjects"))
        ).isTrue());
        test1.addTest(result -> assertThat(
                result.getResult().getErrors().stream().anyMatch(error -> error.contains("company"))
        ).isTrue());

        GeocoderDto success = this.getLastCreateObjectClone();
        success.getGeoObjects().remove(0);
        int sizeGeoObjects = success.getGeoObjects().size();
        success.setName("Самарская область, Волжский район, квартал Южный Город-1, Новая 5");
        success.setLongitude(55.0);
        success.setLatitude(55.0);
        TestCase<GeocoderDto, GeocoderDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getGeoObjects().size()).isEqualTo(sizeGeoObjects));
        test2.addTest(result -> assertThat(result.getResult().getName())
                .isEqualTo("Самарская область, Волжский район, квартал Южный Город-1, Новая 5"));
        test2.addTest(result -> assertThat(result.getResult().getLatitude()).isEqualTo(55.0));
        test2.addTest(result -> assertThat(result.getResult().getLongitude()).isEqualTo(55.0));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> patchGetTestCases() {
        GeocoderDto createObject = this.getLastCreateObjectClone();
        int goSize = createObject.getGeoObjects().size();

        GeocoderDto fail1 = new GeocoderDto();
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("uuid")).isTrue());

        GeocoderDto success = new GeocoderDto();
        success.setUuid(createObject.getUuid());
        success.setGeoObjects(createObject.getGeoObjects());
        success.setName("Test");
        success.getGeoObjects().remove(0);

        TestCase<GeocoderDto, GeocoderDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(createObject.getUuid()));
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test2.addTest(result -> assertThat(result.getResult().getGeoObjects().size()).isEqualTo(goSize));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> deleteSoftGetTestCases() {
        GeocoderDto fail1 = new GeocoderDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("GeocoderEntity")).isTrue());

        GeocoderDto fail2 = new GeocoderDto();
        fail2.setUuid(null);
        TestCase<GeocoderDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        GeocoderDto success = this.getLastCreateObjectClone();
        TestCase<GeocoderDto, GeocoderDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> restoreGetTestCases() {
        GeocoderDto fail1 = new GeocoderDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<GeocoderDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("GeocoderEntity")).isTrue());

        GeocoderDto fail2 = new GeocoderDto();
        fail2.setUuid(null);
        TestCase<GeocoderDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        GeocoderDto success = this.getLastCreateObjectClone();
        TestCase<GeocoderDto, GeocoderDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
