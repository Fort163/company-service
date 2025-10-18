CREATE TABLE employee
(
    uuid          uuid primary key,
    created_by    varchar(255),
    created_when  timestamp,
    updated_by    varchar(255),
    updated_when  timestamp,
    is_active     boolean,
    auth_id        uuid not null,
    profession_id uuid not null
        constraint employee2profession_id_ref
            references profession,
    company_id    uuid not null
        constraint employee2company_id_ref
            references company,
    unique (auth_id, company_id, profession_id)
);

CREATE TABLE employee_permissions
(
    employee_id uuid         not null,
    permission  varchar(255) not null,
    primary key (employee_id, permission),
    foreign  key (employee_id) references employee
);