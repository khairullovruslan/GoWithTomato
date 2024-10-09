CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(15)
);

CREATE TABLE  rating(
    id SERIAL PRIMARY KEY,
    count_trips INT DEFAULT 0,
    sum_rates INT DEFAULT 0,
    user_id INT REFERENCES users(id) ON DELETE CASCADE
);

CREATE TYPE trip_status AS ENUM ('available', 'completed', 'cancelled');


CREATE TABLE route (
    id SERIAL PRIMARY KEY,
    departure_point VARCHAR(255) NOT NULL,
    destination_point VARCHAR(255) NOT NULL,
    intermediate_stops TEXT,
    distance DECIMAL(10, 2)
);
CREATE TABLE trip(
                     id SERIAL PRIMARY KEY,
                     user_id INT REFERENCES users(id) ON DELETE CASCADE,
                     route_id INT REFERENCES route(id) ON DELETE CASCADE,
                     trip_date_time TIMESTAMP NOT NULL,
                     available_seats INT NOT NULL,
                     price DECIMAL(10, 2) NOT NULL,
                     status trip_status
);

CREATE TABLE review(
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    trip_id INT REFERENCES trip(id) ON DELETE  CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    description text
)
