CREATE TABLE service (
       uuid uuid primary key,
       created_by         varchar(255),
       created_when       timestamp,
       updated_by         varchar(255),
       updated_when       timestamp ,
       name               varchar(255),
       work_clock         time,
       count_part_time    smallint,
       company_id         uuid not null
           constraint service2company_id_ref
               references company,
       is_active boolean
);