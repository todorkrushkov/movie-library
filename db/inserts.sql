USE movie_library;

INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN'),
       ('john', 'john123', 'USER'),
       ('maria', 'maria123', 'USER');

INSERT INTO movies (title, director, release_year, rating)
VALUES ('The Matrix', 'Lana Wachowski, Lilly Wachowski', 1999, 8.7),
       ('Inception', 'Christopher Nolan', 2010, 8.8),
       ('Interstellar', 'Christopher Nolan', 2014, NULL),
       ('Fight Club', 'David Fincher', 1999, NULL),
       ('The Dark Knight', 'Christopher Nolan', 2008, 9.0);