package com.quick.recording.company.service.activity;

import com.quick.recording.company.service.TestAuthHelper;
import com.quick.recording.company.service.controller.ActivityControllerImpl;
import com.quick.recording.resource.service.security.SSOService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor
public class ActivityTestController {

    private TestAuthHelper authHelper;

    @LocalServerPort
    private int port;

    private ActivityControllerImpl controller;
    private TestRestTemplate restTemplate;


    @Autowired
    public ActivityTestController(SSOService ssoService, ActivityControllerImpl controller,
                                  TestRestTemplate restTemplate) {
        this.controller = controller;
        this.authHelper = new TestAuthHelper(ssoService);
        this.restTemplate = restTemplate;
    }

    @Test
    public void init() {
        assertThat(port).isNotNull();
        assertThat(controller).isNotNull();
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void authServerForTest() {
        assertThat(authHelper.token()).isNotNull();
    }


    @Test
    public void testGetList() {
    }

}
