create table PROJECT_WORKERS(
    id serial primary key,
    projectID integer not null,
    workerID integer not null
)