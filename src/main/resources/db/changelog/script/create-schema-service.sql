CREATE TABLE service
(
    uuid            uuid primary key,
    created_by      varchar(255),
    created_when    timestamp,
    updated_by      varchar(255),
    updated_when    timestamp,
    is_active       boolean,
    name            varchar(255),
    work_clock      time,
    count_part_time smallint,
    company_id      uuid not null
        constraint service2company_id_ref
            references company,
    employee_id      uuid
        constraint service2employee_id_ref
            references employee,
    unique (name, company_id, employee_id)
);

CREATE TABLE employee2service
(
    employee2service_id uuid not null
        constraint employee2service_id_ref
            references employee,
    service2employee_id uuid not null
        constraint service2employee_id_ref
            references service
);

CREATE TABLE profession2service
(
    profession2service_id uuid not null
        constraint profession2service_id_ref
            references profession,
    service2profession_id uuid not null
        constraint service2profession_id_ref
            references service
);
