DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS tasks;

CREATE TABLE books (
	id          SERIAL PRIMARY KEY ,
	title       TEXT,
	author      TEXT,
	pages_count INT
);

CREATE TABLE tasks (
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    createDate  TIMESTAMP,
    completed   BOOLEAN
);