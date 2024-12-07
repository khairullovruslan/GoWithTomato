CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    login        VARCHAR(100) UNIQUE NOT NULL,
    password     VARCHAR(255),
    email        VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(15),
    photo_url varchar(256)

);

CREATE TABLE rating
(
    id          SERIAL PRIMARY KEY,
    count_trips INT DEFAULT 0,
    sum_rates   INT DEFAULT 0,
    user_id     INT REFERENCES users (id) ON DELETE CASCADE
);
CREATE TABLE Point
(
    id        SERIAL PRIMARY KEY,
    lat       DOUBLE PRECISION,
    lng       DOUBLE PRECISION,
    name      VARCHAR(255),
    country   VARCHAR(100),
    state     VARCHAR(100),
    osm_value VARCHAR(255),
    CONSTRAINT unique_lat_lng UNIQUE (lat, lng)
);

CREATE TYPE trip_status AS ENUM ('available', 'completed', 'cancelled');


CREATE TABLE route
(
    id              SERIAL PRIMARY KEY,
    user_id         INT REFERENCES users (id) ON DELETE CASCADE,
    start_point_id  INT REFERENCES Point (id) ON DELETE RESTRICT,
    finish_point_id INT REFERENCES Point (id) ON DELETE RESTRICT,
    distance        DECIMAL(10, 2)
);



CREATE TABLE trip
(
    id              SERIAL PRIMARY KEY,
    user_id         INT REFERENCES users (id) ON DELETE CASCADE,
    route_id        INT REFERENCES route (id) ON DELETE CASCADE,
    trip_date_time  TIMESTAMP      NOT NULL,
    available_seats INT            NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    status          trip_status
);

CREATE TABLE review
(
    id          SERIAL PRIMARY KEY,
    user_id     INT REFERENCES users (id) ON DELETE CASCADE,
    trip_id     INT REFERENCES trip (id) ON DELETE CASCADE,
    rating      INT CHECK (rating BETWEEN 1 AND 5),
    description text CHECK (length(description) <= 350)

);


CREATE TABLE trip_participants
(
    trip_id INT REFERENCES trip (id) ON DELETE CASCADE,
    user_id INT REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (trip_id, user_id)
);

CREATE TABLE route_intermediate_points
(
    route_id INT REFERENCES route (id) ON DELETE CASCADE,
    point_id INT REFERENCES Point (id) ON DELETE CASCADE,
    sequence INT NOT NULL,
    PRIMARY KEY (route_id, point_id, sequence)
);