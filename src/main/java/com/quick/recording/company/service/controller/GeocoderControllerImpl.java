package com.quick.recording.company.service.controller;

import com.quick.recording.company.service.entity.GeocoderEntity;
import com.quick.recording.company.service.service.GeocoderService;
import com.quick.recording.gateway.dto.yandex.GeocoderDto;
import com.quick.recording.gateway.main.controller.MainControllerAbstract;
import com.quick.recording.gateway.service.company.CompanyServiceGeocoderApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/geocoder")
public class GeocoderControllerImpl
        extends MainControllerAbstract<GeocoderDto, GeocoderEntity, GeocoderService>
        implements CompanyServiceGeocoderApi {

    @Autowired
    public GeocoderControllerImpl(GeocoderService service) {
        super(service);
    }

}