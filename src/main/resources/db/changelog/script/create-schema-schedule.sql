CREATE TABLE schedule
(
    uuid         uuid primary key,
    created_by   varchar(255),
    created_when timestamp,
    updated_by   varchar(255),
    updated_when timestamp,
    is_active    boolean,
    clock_from   time,
    clock_to     time,
    work         boolean,
    day_of_week  varchar(50),
    company_id   uuid not null
        constraint schedule2company_id_ref
            references company,
    employee_id   uuid
        constraint schedule2employee_id_ref
            references employee,
    unique (day_of_week, company_id, employee_id)
);