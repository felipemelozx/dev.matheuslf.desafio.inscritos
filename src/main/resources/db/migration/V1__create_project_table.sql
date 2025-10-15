CREATE TABLE tb_project (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL CHECK (LENGTH(name) >= 3),
    description	VARCHAR(255),
    start_date DATE,
    end_date DATE
);