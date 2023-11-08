package com.quick.recording.company.service.mapper;

import com.quick.recording.company.service.entity.CompanyEntity;
import com.quick.recording.gateway.dto.company.CompanyDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CompanyMapper {

    CompanyEntity companyDtoToCompanyEntity(CompanyDto companyDto);

    CompanyDto companyEntityToCompanyDto(CompanyEntity companyEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "uuid", source = "uuid", ignore = true)
    CompanyEntity updateCompanyEntityFromCompanyDto (CompanyDto companyDto, @MappingTarget CompanyEntity companyEntity);

}
