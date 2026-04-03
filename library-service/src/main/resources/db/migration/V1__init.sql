-- Library tables

CREATE TABLE library (
    id varchar(36) NOT NULL,
    organization_id varchar(36) NOT NULL,
    name varchar(255),
    description text,
    address varchar(255),
    city varchar(255),
    version bigint,
    CONSTRAINT library_pkey PRIMARY KEY (id)
);

CREATE TABLE book (
    id varchar(36) NOT NULL,
    organization_id varchar(36) NOT NULL,
    title varchar(255) NOT NULL,
    author varchar(255),
    isbn varchar(20),
    page_count integer,
    published_year integer,
    version bigint,
    CONSTRAINT book_pkey PRIMARY KEY (id)
);

CREATE TABLE student (
    id varchar(36) NOT NULL,
    organization_id varchar(36) NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    phone_number varchar(50),
    student_number varchar(50),
    year integer,
    department varchar(100),
    version bigint,
    CONSTRAINT student_pkey PRIMARY KEY (id)
);

CREATE TABLE student_address (
    id varchar(36) NOT NULL,
    student_id varchar(36) NOT NULL,
    street varchar(255),
    city varchar(255),
    postcode varchar(50),
    CONSTRAINT student_address_pkey PRIMARY KEY (id)
);

-- Foreign keys
ALTER TABLE student_address ADD CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(id);

-- Audit trail
CREATE TABLE audit_trail (
    id varchar(36) NOT NULL,
    organization_id varchar(36) NOT NULL,
    object_type varchar(255) NOT NULL,
    object_id varchar(36) NOT NULL,
    operation varchar(50) NOT NULL,
    created_by varchar(255),
    created_at timestamptz,
    modified_by varchar(255),
    modified_at timestamptz,
    old_value text,
    new_value text,
    CONSTRAINT audit_trail_pkey PRIMARY KEY (id)
);

CREATE INDEX idx_library_city ON library(city);
CREATE INDEX idx_library_name ON library(name);
CREATE INDEX idx_library_description ON library(description);
CREATE INDEX idx_library_address ON library(address);

CREATE INDEX idx_book_author ON book(author);
CREATE INDEX idx_book_title ON book(title);
CREATE INDEX idx_book_isbn ON book(isbn);

CREATE INDEX idx_student_first_name ON student(first_name);
CREATE INDEX idx_student_last_name ON student(last_name);
CREATE INDEX idx_student_email ON student(email);
CREATE INDEX idx_student_department ON student(department);
CREATE INDEX idx_student_number ON student(student_number);

CREATE INDEX idx_audit_object_type ON audit_trail(object_type);
CREATE INDEX idx_audit_object_id ON audit_trail(object_id);

-- Demo data
INSERT INTO library (organization_id, name, description, address, city) VALUES
    ('0', 'Downtown Public Library', 'Located in downtown area with extensive collections', '123 Main Street', 'NY'),
    ('0', 'Central Library', 'Central branch with many resources', '456 Central Ave', 'CA');

INSERT INTO book (organization_id, title, author, isbn, page_count, published_year) VALUES
    ('0', 'The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 180, 1925),
    ('0', '1984', 'George Orwell', '978-0451524935', 256, 1949),
    ('0', 'To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 324, 1960);

INSERT INTO student (organization_id, first_name, last_name, email, phone_number, student_number, year, department) VALUES
    ('0', 'John', 'Doe', 'john.doe@example.com', '555-0100', 'STU001', 2024, 'Computer Science'),
    ('0', 'Jane', 'Smith', 'jane.smith@example.com', '555-0101', 'STU002', 2024, 'Engineering');
