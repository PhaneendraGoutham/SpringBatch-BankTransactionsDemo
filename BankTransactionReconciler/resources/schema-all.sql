DROP TABLE people IF EXISTS;

CREATE TABLE customertransactions  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    account NUMERIC(10),
    amount NUMERIC(12,2)
);