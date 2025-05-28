CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL
);

CREATE TABLE events (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        date DATE NOT NULL,
                        category VARCHAR(50) NOT NULL,
                        location VARCHAR(100) NOT NULL,
                        seats_total INT NOT NULL,
                        seats_taken INT DEFAULT 0
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                              event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
                              reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
