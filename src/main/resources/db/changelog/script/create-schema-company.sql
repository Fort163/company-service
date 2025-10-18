CREATE TABLE company
(
    uuid         uuid primary key,
    created_by   varchar(100),
    created_when timestamp,
    updated_by   varchar(100),
    updated_when timestamp,
    is_active    boolean,
    name         varchar(255) not null,
    description  varchar(1024)
);

CREATE TABLE company2activity
(
    company2activity_id uuid not null
        constraint company2activity_id_ref
            references company,
    activity2company_id uuid not null
        constraint activity2company_id_ref
            references activity
);