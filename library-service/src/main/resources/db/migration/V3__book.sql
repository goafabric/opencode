-- Flyway migration V3: Create book table
create table "book" (
    id                varchar(36) not null constraint pk_book primary key,
    organization_id   varchar(36) not null,
    
    isbn              varchar(50) not null unique,
    title             varchar(500) not null,
    author            varchar(255) not null,
    publisher         varchar(255),
    publication_year  integer,
    
    version           bigint default 0
);

create index idx_book_organization_id on "book"(organization_id);
create index idx_book_isbn on "book"(isbn);
create index idx_book_title on "book"(title);
create index idx_book_author on "book"(author);

-- Comments for schema documentation
comment on column "book".isbn is 'International Standard Book Number, unique identifier';
comment on table "book" is 'Stores book information including metadata';
