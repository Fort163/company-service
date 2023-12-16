CREATE TABLE schedule (
       uuid uuid primary key,
       created_by         varchar(255),
       created_when       timestamp,
       updated_by         varchar(255),
       updated_when       timestamp ,
       clock_from          time,
       clock_to            time,
       work               boolean,
       day_of_week          varchar(100),
       company_id         uuid not null
           constraint schedule2company_id_ref
               references company,
       is_active boolean
);