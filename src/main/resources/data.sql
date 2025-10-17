INSERT INTO movies (title, description, release_date, rating) VALUES
('The Matrix', 'A computer programmer discovers reality is a simulation and joins a rebellion against machines.', '1999-03-31', 8.7),
('Inception', 'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.', '2010-07-16', 8.8),
('Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity survival.', '2014-11-07', 8.6),
('The Dark Knight', 'Batman faces the Joker, a criminal mastermind who wants to plunge Gotham City into anarchy.', '2008-07-18', 9.0),
('Pulp Fiction', 'The lives of two mob hitmen, a boxer, and others intertwine in four tales of violence and redemption.', '1994-10-14', 8.9);

INSERT INTO movie_genres (movie_id, genre) VALUES
(1, 'Action'), (1, 'Sci-Fi'),
(2, 'Action'), (2, 'Sci-Fi'), (2, 'Thriller'),
(3, 'Adventure'), (3, 'Drama'), (3, 'Sci-Fi'),
(4, 'Action'), (4, 'Crime'), (4, 'Drama'),
(5, 'Crime'), (5, 'Drama');

INSERT INTO users (username) VALUES
('alice'), ('bob'), ('charlie');

INSERT INTO ratings (user_id, movie_id, score) VALUES
(1, 1, 5.0), (1, 2, 4.5), (1, 4, 4.8),
(2, 1, 4.2), (2, 3, 4.7), (2, 5, 4.1),
(3, 2, 5.0), (3, 3, 4.3), (3, 4, 4.9);