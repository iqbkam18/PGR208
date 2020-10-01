create table WORKER_TASK(
    id serial primary key,
    workerID integer not null,
    taskID integer not null
)