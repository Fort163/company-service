package com.quick.recording.company.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Company System Api",
                description = "Quick Recording System", version = "1.0.0",
                contact = @Contact(
                        name = "QUICK",
                        email = "test@test.ru",
                        url = "http://quick_recording"
                )
        )
)
public class OpenApiConfig {

}
