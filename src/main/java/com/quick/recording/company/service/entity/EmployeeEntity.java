package com.quick.recording.company.service.entity;

import com.quick.recording.gateway.entity.SmartEntity;
import com.quick.recording.gateway.enumerated.CompanyHierarchyEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity(name = "employee")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"company","services"})
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends SmartEntity {

    @Column(name = "auth_id", nullable = false)
    private UUID authId;

    @ManyToOne(optional = false, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "profession_id")
    private ProfessionEntity profession;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    @CollectionTable(
            name = "employee_permissions",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @ElementCollection(targetClass = CompanyHierarchyEnum.class, fetch = FetchType.EAGER)
    private List<CompanyHierarchyEnum> permissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee2service",
            joinColumns = {@JoinColumn(name = "employee2service_id")},
            inverseJoinColumns = {@JoinColumn(name = "service2employee_id")}
    )
    private List<ServiceEntity> services;


}
