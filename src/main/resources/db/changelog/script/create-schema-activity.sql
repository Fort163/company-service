CREATE TABLE activity
(
    uuid         uuid primary key,
    created_by   varchar(255),
    created_when timestamp,
    updated_by   varchar(255),
    updated_when timestamp,
    name         varchar(255)
        constraint uk_activity_name unique,
    description  varchar(255)
        constraint uk_activity_description unique,
    is_active    boolean
);

insert into activity
values (gen_random_uuid(), 'company-service', current_timestamp, 'company-service',
        current_timestamp, 'СТО', 'Станция технического обслуживания', true);
insert into activity
values (gen_random_uuid(), 'company-service', current_timestamp, 'company-service',
        current_timestamp, 'Салон красоты', 'Салон красоты', true);
insert into activity
values (gen_random_uuid(), 'company-service', current_timestamp, 'company-service',
        current_timestamp, 'Шиномонтаж', 'Шиномонтаж', true);
insert into activity
values (gen_random_uuid(), 'company-service', current_timestamp, 'company-service',
        current_timestamp, 'Стоматология', 'Стоматология', true);
insert into activity
values (gen_random_uuid(), 'company-service', current_timestamp, 'company-service',
        current_timestamp, 'Частная клиника', 'Частная клиника', true);