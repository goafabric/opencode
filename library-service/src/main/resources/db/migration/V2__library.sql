create table library
(
	id varchar(36) not null
		constraint pk_library
			primary key,
    organization_id varchar(36) not null,

	name varchar(255),
	address varchar(255),

    version bigint default 0
);

create index idx_library_organization_id on library(organization_id);
create index idx_library_name on library(name);

create table book
(
	id varchar(36) not null
		constraint pk_book
			primary key,
    organization_id varchar(36) not null,

	title varchar(255),
	author varchar(255),
	isbn varchar(20),
	library_id varchar(36),

    version bigint default 0,

    constraint fk_book_library foreign key (library_id) references library(id)
);

create index idx_book_organization_id on book(organization_id);
create index idx_book_title on book(title);
create index idx_book_author on book(author);
create index idx_book_isbn on book(isbn);
create index idx_book_library_id on book(library_id);

create table student
(
	id varchar(36) not null
		constraint pk_student
			primary key,
    organization_id varchar(36) not null,

	first_name varchar(255),
	last_name varchar(255),
	email varchar(255),

    version bigint default 0
);

create index idx_student_organization_id on student(organization_id);
create index idx_student_first_name on student(first_name);
create index idx_student_last_name on student(last_name);
create index idx_student_email on student(email);

create table book_lending
(
	id varchar(36) not null
		constraint pk_book_lending
			primary key,
    organization_id varchar(36) not null,

	student_id varchar(36),
	book_id varchar(36),
	borrow_date date,
	due_date date,
	return_date date,

    version bigint default 0,

    constraint fk_lending_student foreign key (student_id) references student(id),
    constraint fk_lending_book foreign key (book_id) references book(id)
);

create index idx_lending_organization_id on book_lending(organization_id);
create index idx_lending_student_id on book_lending(student_id);
create index idx_lending_book_id on book_lending(book_id);
