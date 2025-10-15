CREATE TABLE tb_task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL CHECK (LENGTH(title) >= 5),
    description TEXT,
    status VARCHAR(10) NOT NULL CHECK (status IN ('TODO', 'DOING', 'DONE')),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date DATE,
    project_id INT REFERENCES tb_project(id) ON DELETE CASCADE
);