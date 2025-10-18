package com.quick.recording.company.service;

import com.quick.recording.company.service.integration.ActivityTestController;
import com.quick.recording.company.service.integration.CompanyTestController;
import com.quick.recording.company.service.integration.FinishIntegration;
import com.quick.recording.company.service.integration.GeocoderTestController;
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
        FinishIntegration.class
})
@ActiveProfiles("test")
class CompanyServiceAppSuiteTest {


}
