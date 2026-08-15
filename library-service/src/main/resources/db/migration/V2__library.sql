create table student
(
	id varchar(36) not null
		constraint pk_student
			primary key,

	first_name varchar(255),
	last_name varchar(255),
	matriculation_number varchar(255),

    version bigint default 0
);

create index idx_student_first_name on student(first_name);
create index idx_student_last_name on student(last_name);
create index idx_student_matriculation_number on student(matriculation_number);

create table library
(
	id varchar(36) not null
		constraint pk_library
			primary key,

	name varchar(255),
	city varchar(255),

    version bigint default 0
);

create index idx_library_name on library(name);
create index idx_library_city on library(city);

create table book
(
	id varchar(36) not null
		constraint pk_book
			primary key,

	isbn varchar(255),
	title varchar(255),
	author varchar(255),
	library_id varchar(36),
	student_id varchar(36),

    version bigint default 0
);

create index idx_book_title on book(title);
create index idx_book_author on book(author);
create index idx_book_isbn on book(isbn);
create index idx_book_library_id on book(library_id);
create index idx_book_student_id on book(student_id);
