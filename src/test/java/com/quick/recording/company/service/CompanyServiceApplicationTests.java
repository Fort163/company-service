package com.quick.recording.company.service;

import static org.assertj.core.api.Assertions.*;

import com.quick.recording.resource.service.security.SSOService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CompanyServiceApplicationTests {


    private TestAuthHelper authHelper;

    @Autowired
    public CompanyServiceApplicationTests(SSOService ssoService) {
        this.authHelper = new TestAuthHelper(ssoService);
    }

    @Test
    void loadContext() {
    }

    @Test
    void authServerForTest() {
        assertThat(authHelper.token()).isNotNull();
    }

}
