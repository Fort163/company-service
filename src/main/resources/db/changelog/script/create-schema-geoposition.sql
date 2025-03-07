CREATE TABLE geocoder
(
    uuid         uuid primary key,
    created_by   varchar(255),
    created_when timestamp,
    updated_by   varchar(255),
    updated_when timestamp,
    longitude    decimal,
    latitude     decimal,
    name         varchar(1000),
    company_id   uuid not null
        constraint geocoder2company_id_ref
            references company
);

CREATE TABLE geocoder_object
(
    uuid        uuid primary key,
    name        varchar(255),
    kind        varchar(255),
    geocoder_id uuid not null
        constraint geocoder_object2geocoder_id_ref
            references geocoder
);