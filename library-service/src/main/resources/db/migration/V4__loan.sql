-- Flyway migration V4: Create loan table
create table loan (
    id                 varchar(36) not null constraint pk_loan primary key,
    organization_id    varchar(36) not null,
    
    student_id         varchar(36) not null constraint fk_loan_student foreign key references student(id) on delete cascade,
    book_id            varchar(36) not null constraint fk_loan_book foreign key references "book"(id) on delete set null,
    
    loan_date          date not null default current_date,
    return_date        date,
    due_date           date not null,
    status             varchar(20) not null default 'ACTIVE' check (status IN ('ACTIVE', 'RETURNED', 'OVERDUE')),
    
    version            bigint default 0
);

create index idx_loan_organization_id on loan(organization_id);
create index idx_loan_student_id on loan(student_id);
create index idx_loan_book_id on loan(book_id);
create index idx_loan_status on loan(status);
create index idx_loan_due_date on loan(due_date);

-- Comments for schema documentation
comment on column loan.student_id is 'Reference to the student borrowing the book';
comment on column loan.book_id is 'Reference to the book being borrowed';
comment on column loan.status is 'Loan status: ACTIVE, RETURNED, or OVERDUE';
comment on table loan is 'Tracks book loans, return dates, and loan status';
