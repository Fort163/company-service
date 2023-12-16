CREATE TABLE company (
                         uuid uuid primary key,
                         created_by         varchar(255),
                         created_when       timestamp,
                         updated_by         varchar(255),
                         updated_when       timestamp ,
                         name               varchar(255),
                         is_active boolean
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