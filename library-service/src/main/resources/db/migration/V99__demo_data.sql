-- V99__demo_data.sql
-- Demo data for library service

-- Add some demo students
INSERT INTO students (student_id, name, library) VALUES
    ('ST001', 'Alice Johnson', 'Main Library'),
    ('ST002', 'Bob Smith', 'Main Library'),
    ('ST003', 'Charlie Brown', 'Central Library'),
    ('ST004', 'Diana Ross', 'Main Library'),
    ('ST005', 'Eve Wilson', 'Central Library');

-- Add some demo books
INSERT INTO books (title, author, isbn, borrowed_by, borrowed_on, due_date, is_borrowed, price) VALUES
    ('Java Programming', 'John Lee', '978-1234567890', NULL, NULL, '2026-05-01', FALSE, 49.99),
    ('Spring Boot Guide', 'Sarah Smith', '978-1234567891', 'ST001', '2026-01-01', '2026-02-01', TRUE, 39.99),
    ('Database Design', 'Mike Johnson', '978-1234567892', NULL, NULL, NULL, FALSE, 54.99),
    ('Clean Code', 'Robert Martin', '978-1234567893', 'ST002', '2026-01-02', '2026-02-02', TRUE, 29.99),
    ('Design Patterns', 'Erich Gamma', '978-1234567894', NULL, NULL, NULL, FALSE, 44.99),
    ('Microservices Architecture', 'James Wilson', '978-1234567895', 'ST003', '2026-01-03', '2026-02-03', TRUE, 34.99),
    ('Clean Architecture', 'Robert C. Martin', '978-1234567896', NULL, NULL, NULL, FALSE, 39.99),
    ('Domain Driven Design', 'Eric Evans', '978-1234567897', 'ST004', '2026-01-04', '2026-02-04', TRUE, 49.99),
    ('Refactoring', 'Martin Fowler', '978-1234567898', NULL, NULL, NULL, FALSE, 34.99),
    ('The Pragmatic Programmer', 'David Thomas', '978-1234567899', 'ST005', '2026-01-05', '2026-02-05', TRUE, 29.99);

-- Add some demo libraries
INSERT INTO libraries (name, address, phone, email) VALUES
    ('Main Library', '123 Library St, Main City', '555-1234', 'main.library@example.com'),
    ('Central Library', '456 Central Ave, Central City', '555-5678', 'central.library@example.com'),
    ('East Library', '789 East Rd, East Town', '555-9012', 'east.library@example.com');
