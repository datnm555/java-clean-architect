create table customers (
    id            uuid primary key,
    name          varchar(255) not null,
    email         varchar(255) not null unique,
    registered_at timestamptz  not null
);
