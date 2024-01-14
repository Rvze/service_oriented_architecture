create table venues
(
    id       bigserial primary key,
    name     varchar(100),
    capacity int,
    type     varchar(20)
);

create table events
(
    id   bigserial primary key,
    name varchar(100)
);

create table persons
(
    id   bigserial primary key,
    name varchar(100)
);

create table tickets
(
    id            bigserial primary key,
    name          varchar(100),
    coordinates   jsonb,
    creation_date timestamptz,
    price         int,
    type          varchar(20),
    venue         varchar(20),
    venue_id      bigint,
    event_id      bigint,
    person_id     bigint,
    constraint fk_venue_id foreign key (venue_id) references venues (id) on delete cascade,
    constraint fk_event_id foreign key (event_id) references events (id)
);