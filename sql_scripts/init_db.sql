-- DROP DATABASE IF EXISTS resumes;
-- CREATE USER basejava_user WITH PASSWORD 'xxx';
-- CREATE DATABASE resumes;
-- GRANT ALL PRIVILEGES ON DATABASE resumes TO basejava_user;

create table resume
(
    uuid      char(36) not null
        constraint uuid
            primary key,
    full_name text     not null
);

-- alter table resume
--     owner to basejava_user;

create unique index primary_key
    on resume (uuid);


create table contact
(
    id          serial,
    resume_uuid char(36) not null
        references resume
            on delete cascade,
    type        text     not null,
    value       text     not null
);

-- alter table contact
--     owner to basejava_user;

create unique index contact_uuid_type_index
    on contact (resume_uuid, type);


create table section
(
    id         serial,
    resume_uuid char(36)                                                 not null
        references resume
            on delete cascade,
    value       text                                                     not null,
    type        text                                                    not null
);

-- alter table section
--     owner to basejava_user;

create index resume_uuid_index
    on section (resume_uuid);

