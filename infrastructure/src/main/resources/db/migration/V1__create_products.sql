create table products (
    id         uuid primary key,
    name       varchar(255)   not null,
    price      numeric(12, 2) not null,
    created_at timestamptz    not null
);
