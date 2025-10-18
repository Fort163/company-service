package com.quick.recording.company.service.service;

import com.quick.recording.company.service.entity.EmployeeEntity;
import com.quick.recording.company.service.mapper.EmployeeMapper;
import com.quick.recording.company.service.repository.entity.EmployeeEntityRepository;
import com.quick.recording.gateway.config.MessageUtil;
import com.quick.recording.gateway.dto.company.EmployeeDto;
import com.quick.recording.gateway.main.service.local.CacheableMainServiceAbstract;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class EmployeeServiceImpl
        extends CacheableMainServiceAbstract<EmployeeEntity, EmployeeDto, EmployeeEntityRepository, EmployeeMapper>
        implements EmployeeService {

    private final ProfessionService professionService;

    public EmployeeServiceImpl(ProfessionService professionService,
                               EmployeeEntityRepository repository,
                               EmployeeMapper mapper,
                               MessageUtil messageUtil,
                               StreamBridge streamBridge) {
        super(repository, mapper, messageUtil, EmployeeEntity.class, streamBridge);
        this.professionService = professionService;
    }

    @Override
    public ExampleMatcher prepareExampleMatcher(ExampleMatcher exampleMatcher) {
        return exampleMatcher;
    }

    @Override
    @Transactional(
            propagation = Propagation.MANDATORY
    )
    protected void afterPost(EmployeeEntity entity, EmployeeDto dto) {
        if(Objects.nonNull(entity.getProfession()) && Objects.nonNull(entity.getProfession().getUuid())) {
            entity.setProfession(this.getProfessionService().byUuidEntity(entity.getProfession().getUuid()));
        }
    }

    @Override
    public Class<EmployeeDto> getType() {
        return EmployeeDto.class;
    }

    public ProfessionService getProfessionService() {
        return professionService;
    }
}

