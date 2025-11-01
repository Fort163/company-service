package com.quick.recording.company.service;

import com.quick.recording.company.service.service.local.*;
import com.quick.recording.gateway.dto.company.*;
import com.quick.recording.gateway.dto.schedule.ScheduleDto;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.dto.yandex.GeocoderObjectDto;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import com.quick.recording.gateway.enumerated.DayOfWeek;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class CompanyServiceAppFactory {

    public static ActivityDto createActivity(ActivityService service){
        ActivityDto dto = new ActivityDto();
        dto.setName("Тестовый вид деятельности");
        dto.setDescription("Описание вида деятельности");
        return service.post(dto);
    }

    public static CompanyDto createCompany(CompanyService service, ActivityDto activityDto){
        CompanyDto dto = new CompanyDto();
        dto.setName("Тестовая компания");
        dto.setActivities(List.of(activityDto));
        return service.post(dto);
    }

    public static ServiceDto createService(ServiceService service, CompanyDto companyDto){
        ServiceDto dto = new ServiceDto();
        dto.setName("Тестовая услуга");
        dto.setCompany(companyDto);
        dto.setWorkClock(LocalTime.of(1, 0));
        return service.post(dto);
    }

    public static ScheduleDto createSchedule(ScheduleService service, CompanyDto companyDto){
        ScheduleDto dto = new ScheduleDto();
        dto.setClockFrom(LocalTime.of(8, 0));
        dto.setClockTo(LocalTime.of(18, 0));
        dto.setDayOfWeek(DayOfWeek.monday);
        dto.setWork(true);
        dto.setCompany(companyDto);
        return service.post(dto);
    }

    public static EmployeeDto createEmployee(EmployeeService service, CompanyDto companyDto,
                                             ProfessionDto professionDto, ServiceDto serviceDto){
        EmployeeDto dto = new EmployeeDto();
        dto.setAuthId(UUID.randomUUID());
        dto.setPermissions(List.of(CompanyHierarchyEnum.OWNER));
        dto.setCompany(companyDto);
        dto.setProfession(professionDto);
        dto.setServices(List.of(serviceDto));
        return service.post(dto);
    }

    public static ProfessionDto createProfession(ProfessionService service, CompanyDto companyDto, ServiceDto serviceDto){
        ProfessionDto dto = new ProfessionDto();
        dto.setName("Тестовая профессия");
        dto.setDescription("Тестовое описание");
        dto.setCompany(companyDto);
        dto.setServices(List.of(serviceDto));
        return service.post(dto);
    }

    public static GeocoderDto createGeocoder(GeocoderService service, CompanyDto companyDto){
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

        GeocoderDto dto = new GeocoderDto();
        dto.setLatitude(50.0);
        dto.setLongitude(50.0);
        dto.setName("Самарская область, Волжский район, квартал Южный Город-1, Лета, 13");
        dto.setCompany(companyDto);
        dto.setGeoObjects(List.of(go1,go2,go3,go4));
        return service.post(dto);
    }

}
