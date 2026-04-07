-- Flyway migration V2: Create student table
create table student (
    id                 varchar(36) not null constraint pk_student primary key,
    organization_id    varchar(36) not null,
    
    matriculation_number  varchar(50) not null unique,
    first_name          varchar(100) not null,
    last_name           varchar(100) not null,
    
   version              bigint default 0
);

create index idx_student_organization_id on student(organization_id);
create index idx_student_matriculation_number on student(matriculation_number);
create index idx_student_first_name on student(first_name);
create index idx_student_last_name on student(last_name);

-- Comments for schema documentation
comment on column student.matriculation_number is 'Unique identifier for the student within the institution';
comment on table student is 'Stores student information including personal details';
