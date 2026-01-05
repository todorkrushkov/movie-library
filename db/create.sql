DROP DATABASE IF EXISTS movie_library;
CREATE DATABASE movie_library CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE movie_library;

CREATE TABLE users
(
    user_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     ENUM ('USER','ADMIN') DEFAULT 'USER'
);

CREATE TABLE movies
(
    movie_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL UNIQUE,
    description  TEXT,
    director     VARCHAR(255),
    release_date DATE,
    rating       DOUBLE
);

CREATE TABLE watched_movies
(
    watched_movie_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    movie_id         BIGINT NOT NULL,
    note             TEXT,
    watched_at       DATE   NOT NULL,

    CONSTRAINT uq_user_movie UNIQUE (user_id, movie_id),

    CONSTRAINT fk_watched_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE,

    CONSTRAINT fk_watched_movie FOREIGN KEY (movie_id)
        REFERENCES movies (movie_id) ON DELETE CASCADE
)