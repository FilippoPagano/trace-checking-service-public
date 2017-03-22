DROP KEYSPACE myDataset_keyspace;
tracing off;
TRUNCATE system_traces.sessions;
TRUNCATE system_traces.events;

CREATE USER tcers WITH PASSWORD 'polimi' SUPERUSER;


CREATE KEYSPACE myDataset_keyspace
           WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

USE myDataset_keyspace;

CREATE TABLE myDataset_keyspace.mydataset (
    Id int,
    Zip text,
    Age int,
    Nationality text,
    Problem text,
    PRIMARY KEY (Id)
);
tracing on;
