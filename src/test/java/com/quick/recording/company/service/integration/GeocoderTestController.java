package com.quick.recording.company.service.integration;

import com.quick.recording.company.service.ContextConstant;
import com.quick.recording.company.service.main.MainTestController;
import com.quick.recording.company.service.main.TestCase;
import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.quick.recording.company.service.ContextConstant.GEOCODER_DTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Test Api Geocoder in company service")
public class GeocoderTestController extends MainTestController<GeocoderDto> {

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
        CompanyDto companyDto = (CompanyDto) getTestContextHolder().getLast(ContextConstant.COMPANY_DTO);
        GeocoderDto fail1 = new GeocoderDto();

        GeocoderDto fail2 = new GeocoderDto();

        GeocoderDto fail3 = new GeocoderDto();

        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> deleteHardGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> byUUIDAfterDeleteGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> listGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> putGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> patchGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> deleteSoftGetTestCases() {
        return null;
    }

    @Override
    public List<TestCase<GeocoderDto, ?>> restoreGetTestCases() {
        return null;
    }
}
