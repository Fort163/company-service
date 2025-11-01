package com.quick.recording.company.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quick.recording.company.service.service.local.ActivityService;
import com.quick.recording.gateway.config.error.ApiError;
import com.quick.recording.gateway.dto.SmartDto;
import com.quick.recording.gateway.dto.company.ActivityDto;
import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.main.service.local.MainService;
import com.quick.recording.gateway.test.MainTestController;
import com.quick.recording.gateway.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Api Activity in company service")
public class ActivityTestController extends MainTestController<ActivityDto> {

    private TypeReference<ActivityDto> typeDto = new TypeReference<ActivityDto>() {};
    private TypeReference<Page<ActivityDto>> typePageDto = new TypeReference<Page<ActivityDto>>() {};

    @Autowired
    private ActivityService activityService;

    @Override
    public List<MainService<? extends SmartEntity, ? extends SmartDto>> getServicesForClear() {
        return List.of(activityService);
    }

    @Override
    public String uri(){
        return "/api/v1/activity";
    }

    @Override
    public List<TestCase<ActivityDto, ?>> postGetTestCases() {

        List<TestCase<ActivityDto, ?>> resultList = new ArrayList<>();

        ActivityDto fail1 = new ActivityDto();
        fail1.setName(null);
        fail1.setDescription("Test description");
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("name")).isTrue());
        resultList.add(test1);

        if(Objects.isNull(this.getLastCreateObjectClone())) {
            ActivityDto success = new ActivityDto();
            success.setName("Activity for list");
            success.setDescription("Activity for list description");
            TestCase<ActivityDto, ActivityDto> test2 = new TestCase<>(success, typeDto);
            test2.addTest(result -> assertThat(result.getCode().is2xxSuccessful()).isTrue());
            test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Activity for list"));
            test2.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
            resultList.add(test2);

            ActivityDto fail2 = new ActivityDto();
            fail2.setName("Activity for list");
            fail2.setDescription("Activity for list description");
            TestCase<ActivityDto, ApiError> test3 = new TestCase<>(fail2, typeError);
            test3.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
            test3.addTest(result -> assertThat(result.getResult().getMessage().contains("duplicate key")).isTrue());
            resultList.add(test3);
        }

        ActivityDto successForTest = new ActivityDto();
        successForTest.setName("Test");
        successForTest.setDescription("Test description");
        TestCase<ActivityDto, ActivityDto> test4 = new TestCase<>(successForTest, typeDto);
        test4.addTest(result -> assertThat(result.getCode().is2xxSuccessful()).isTrue());
        test4.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test4.addTest(result -> assertThat(result.getResult().getUuid()).isNotNull());
        resultList.add(test4);

        return resultList;
    }

    @Override
    public List<TestCase<ActivityDto, ?>> byUUIDBeforeDeleteGetTestCases() {
        ActivityDto fail1 = new ActivityDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ActivityEntity")).isTrue());

        ActivityDto success = this.getLastCreateObjectClone();
        TestCase<ActivityDto, ActivityDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test2.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> deleteHardGetTestCases() {
        ActivityDto fail1 = new ActivityDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ActivityEntity")).isTrue());

        ActivityDto fail2 = new ActivityDto();
        fail2.setUuid(null);
        TestCase<ActivityDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ActivityDto success = this.getLastCreateObjectClone();
        TestCase<ActivityDto, ActivityDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> byUUIDAfterDeleteGetTestCases() {
        ActivityDto fail1 = this.getLastDeletedObjectClone();
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ActivityEntity")).isTrue());

        return List.of(test1);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> listGetTestCases() {
        ActivityDto successAll = new ActivityDto();
        TestCase<ActivityDto, Page<ActivityDto>> test1 = new TestCase<>(successAll, typePageDto);
        test1.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test1.addTest(result -> assertThat(result.getResult()).isNotNull());
        test1.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());

        ActivityDto successOne = new ActivityDto();
        successOne.setName("Test");
        TestCase<ActivityDto, Page<ActivityDto>> test2 = new TestCase<>(successOne, typePageDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().isEmpty()).isFalse());
        test2.addTest(result -> assertThat(result.getResult().getTotalElements()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getContent().get(0).getName()).isEqualTo("Test"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> putGetTestCases() {

        ActivityDto fail1 = this.getLastCreateObjectClone();
        fail1.setName(null);
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("name")).isTrue());

        ActivityDto fail2 = this.getLastCreateObjectClone();
        fail2.setDescription(null);
        TestCase<ActivityDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test2.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("description")).isTrue());

        ActivityDto success = this.getLastCreateObjectClone();
        success.setName("Test new");
        success.setDescription("Test description new");
        TestCase<ActivityDto, ActivityDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test new"));
        test3.addTest(result -> assertThat(result.getResult().getDescription()).isEqualTo("Test description new"));

        return List.of(test1, test2, test3);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> patchGetTestCases() {
        ActivityDto createObject = this.getLastCreateObjectClone();

        ActivityDto fail1 = new ActivityDto();
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getErrors().size()).isEqualTo(1));
        test1.addTest(result -> assertThat(result.getResult().getErrors().get(0).contains("uuid")).isTrue());

        ActivityDto success = new ActivityDto();
        success.setUuid(createObject.getUuid());
        success.setName("Test");
        TestCase<ActivityDto, ActivityDto> test2 = new TestCase<>(success, typeDto);
        test2.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test2.addTest(result -> assertThat(result.getResult()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getCreatedBy()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getUpdatedBy()).isNotNull());
        test2.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(createObject.getUuid()));
        test2.addTest(result -> assertThat(result.getResult().getName()).isEqualTo("Test"));
        test2.addTest(result -> assertThat(result.getResult().getDescription()).isEqualTo("Test description new"));

        return List.of(test1, test2);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> deleteSoftGetTestCases() {
        ActivityDto fail1 = new ActivityDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ActivityEntity")).isTrue());

        ActivityDto fail2 = new ActivityDto();
        fail2.setUuid(null);
        TestCase<ActivityDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ActivityDto success = this.getLastCreateObjectClone();
        TestCase<ActivityDto, ActivityDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isFalse());

        return List.of(test1,test2,test3);
    }

    @Override
    public List<TestCase<ActivityDto, ?>> restoreGetTestCases() {
        ActivityDto fail1 = new ActivityDto();
        fail1.setUuid(UUID.randomUUID());
        TestCase<ActivityDto, ApiError> test1 = new TestCase<>(fail1, typeError);
        test1.addTest(result -> assertThat(result.code().is5xxServerError()).isTrue());
        test1.addTest(result -> assertThat(result.getResult().getMessage().contains("ActivityEntity")).isTrue());

        ActivityDto fail2 = new ActivityDto();
        fail2.setUuid(null);
        TestCase<ActivityDto, ApiError> test2 = new TestCase<>(fail2, typeError);
        test2.addTest(result -> assertThat(result.code().is4xxClientError()).isTrue());
        test2.addTest(result -> assertThat(result.getResult().getMessage().contains("null")).isTrue());

        ActivityDto success = this.getLastCreateObjectClone();
        TestCase<ActivityDto, ActivityDto> test3 = new TestCase<>(success, typeDto);
        test3.addTest(result -> assertThat(result.code().is2xxSuccessful()).isTrue());
        test3.addTest(result -> assertThat(result.getResult().getUuid()).isEqualTo(success.getUuid()));
        test3.addTest(result -> assertThat(result.getResult().getIsActive()).isTrue());

        return List.of(test1,test2,test3);
    }
}
