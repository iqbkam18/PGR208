create table COOKIES(
    id serial primary key,
    cookie varchar(100),
    workerID integer not null
)