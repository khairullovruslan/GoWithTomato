CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(15)
);

create table rating(
    id SERIAL PRIMARY KEY,
    count_trips INT DEFAULT 0,
    sum_rates INT DEFAULT 0,
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
)