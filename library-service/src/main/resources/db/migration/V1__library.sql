-- Flyway Migration for Library Services
create table if not exists student (
    id varchar(255) primary key,
    name text not null,
     version bigint
);

create table if not exists book (
    id varchar(255) primary key,
        title text not null,
         isbn text,
    availableCopies integer default 1,
        version bigint
);
