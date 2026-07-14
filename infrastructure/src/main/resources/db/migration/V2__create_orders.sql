create table orders (
    id          uuid primary key,
    customer_id uuid           not null,
    status      varchar(32)    not null,
    total       numeric(12, 2) not null,
    placed_at   timestamptz    not null
);

create table order_lines (
    order_id   uuid           not null references orders (id),
    product_id uuid           not null,
    quantity   integer        not null,
    unit_price numeric(12, 2) not null
);
