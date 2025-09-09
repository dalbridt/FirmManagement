create table departments(
    id  bigserial primary key,
    dep_name varchar(50) not null,
    manager_id bigint
);

create table employees (
    id  bigserial primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    department_id bigint not null references departments (id) on delete cascade,
    role varchar(50) not null,
    location varchar(50) not null default 'ONLINE' check ( location in ('ONLINE', 'OFFICE')),
    salary numeric not null default 0,
    hire_date date not null default current_date
);

