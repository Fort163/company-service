package com.quick.recording.company.service.controller;

import com.quick.recording.gateway.dto.company.CompanyDto;
import com.quick.recording.gateway.dto.user.UserDto;
import com.quick.recording.gateway.service.company.CompanyController;
import com.quick.recording.gateway.service.user.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyControllerImpl implements CompanyController {

    @Autowired
    private UserController userController;

    @Override
    @GetMapping({"/"})
    public ResponseEntity<CompanyDto> getCompany() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Рога копыта");
        companyDto.setUuid(UUID.randomUUID());
        companyDto.setUsers(userController.usersByCompany(companyDto.getUuid().toString()).getBody());
        ResponseEntity<UserDto> currentUser = userController.getCurrentUser();
        companyDto.getUsers().add(currentUser.getBody());
        return ResponseEntity.ok(companyDto);
    }

    @Override
    @GetMapping({"/list"})
    public ResponseEntity<List<CompanyDto>> getCompanyList() {
        CompanyDto companyDto1 = new CompanyDto();
        companyDto1.setName("Рога копыта");
        companyDto1.setUuid(UUID.randomUUID());
        CompanyDto companyDto2 = new CompanyDto();
        companyDto2.setName("ОТР 2000");
        companyDto2.setUuid(UUID.randomUUID());
        return ResponseEntity.ok(List.of(companyDto1,companyDto2));
    }
}
