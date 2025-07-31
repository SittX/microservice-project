CREATE TABLE t_users(
    id SERIAL NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    isActive boolean,
    primary key(id)
);