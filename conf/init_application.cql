CREATE USER tcers WITH PASSWORD 'polimi' SUPERUSER;


CREATE KEYSPACE application1
           WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

USE application1;

CREATE TABLE table1 (
    Id uuid,
    Race text,
    Birth date,
    Gender text,
    Zip text,
    RegistrationDate timestamp,
    Problem text,
    PRIMARY KEY (Id)
);

CREATE TABLE application1.table1 (
    Name text,
    Birth date,
    Gender text,
    Zip text,
    Address text,
    PRIMARY KEY (Name)
);