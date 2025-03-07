package com.quick.recording.company.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication(scanBasePackages = {"com.quick.recording"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.quick.recording.gateway.service")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@RefreshScope
public class CompanyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyServiceApplication.class, args);
    }

}
