DROP TABLE people IF EXISTS;

CREATE TABLE customertransactions  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    account NUMERIC,
    amount NUMERIC
);