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
    movie_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    director VARCHAR(255) NOT NULL,
    release_year INT NOT NULL,
    rating DOUBLE
);