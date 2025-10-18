CREATE TABLE profession
(
    uuid         uuid primary key,
    created_by   varchar(255),
    created_when timestamp,
    updated_by   varchar(255),
    updated_when timestamp,
    is_active    boolean,
    name   varchar(255),
    description   varchar(255),
    company_id   uuid not null
        constraint profession2company_id_ref
            references company,
    unique (company_id, name)
);