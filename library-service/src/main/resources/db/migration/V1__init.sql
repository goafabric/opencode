-- V1__init.sql
-- Initial schema for library service

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    library VARCHAR(200),
    books JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    book_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    borrowed_by VARCHAR(50),
    borrowed_on TIMESTAMP,
    due_date TIMESTAMP,
    is_borrowed BOOLEAN DEFAULT FALSE,
    price DECIMAL(10, 2) DEFAULT 0
);

-- Libraries table
CREATE TABLE IF NOT EXISTS libraries (
    library_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100)
);

-- Index for library column
CREATE INDEX IF NOT EXISTS idx_students_library ON students(library);

-- Index for name column
CREATE INDEX IF NOT EXISTS idx_students_name ON students(name);
