package com.quick.recording.company.service;

import com.quick.recording.company.service.integration.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.test.context.ActiveProfiles;

@Suite
@SuiteDisplayName("Test Suite for Company Service")
@SelectClasses({
        ActivityTestController.class,
        CompanyTestController.class,
        GeocoderTestController.class,
        ServiceTestController.class,
        ProfessionTestController.class,
        ScheduleTestController.class,
        EmployeeTestController.class,
        FinishIntegration.class
})
@ActiveProfiles("test")
class CompanyServiceAppSuiteTest {

}
