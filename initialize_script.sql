DROP TABLE Friendships;
DROP TABLE Users;
DROP TABLE Messages;

CREATE TABLE Users(
	id BIGINT PRIMARY KEY,
	firstname VARCHAR(256),
	lastname VARCHAR(256),
	username VARCHAR(256),
	passwd VARCHAR(256)
);
CREATE TABLE Friendships(
	userId1 BIGINT,
	userId2 BIGINT,
	data_prieteniei TIMESTAMP,
	status VARCHAR(256),
	FOREIGN KEY(userId1) REFERENCES Users(id) ON DELETE CASCADE,
	FOREIGN KEY(userId2) REFERENCES Users(id) ON DELETE CASCADE,
	PRIMARY KEY (userId1, userId2)
);
CREATE TABLE Messages(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	from_user BIGINT,
	to_users BIGINT[],
	message VARCHAR(256),
	date TIMESTAMP
);

INSERT INTO Users VALUES
(1, 'Alesio', 'Puf', 'alesiopuf', 'alesio123'),
(2, 'Andrei', 'Popescu', 'andreipopescu', 'andrei123'),
(3, 'Maria', 'Ionescu', 'mariaionescu', 'maria123'),
(4, 'Ioana', 'Vasilescu', 'ioanavasilescu', 'ioana123'),
(5, 'Alexandru', 'Petrescu', 'alexpetrescu', 'alex123'),
(6, 'Elena', 'Radu', 'elenaradu', 'elena123'),
(7, 'Cristian', 'Mihai', 'cristianmihai', 'cristian123'),
(8, 'Ana', 'Dumitru', 'anadumitru', 'ana123'),
(9, 'Vlad', 'Georgescu', 'vladgeorgescu', 'vlad123'),
(10, 'Diana', 'Popa', 'dianapopa', 'diana123'),
(11, 'Robert', 'Stan', 'robertstan', 'robert123'),
(12, 'Mihai', 'Ene', 'mihaiene', 'mihai123'),
(13, 'Raluca', 'Iliescu', 'ralucailiescu', 'raluca123'),
(14, 'George', 'Constantin', 'georgeconstantin', 'george123'),
(15, 'Sorina', 'Radulescu', 'sorinaradulescu', 'sorina123'),
(16, 'Daniel', 'Marin', 'danielmarin', 'daniel123'),
(17, 'Alina', 'Cristea', 'alinacristea', 'alina123'),
(18, 'Florin', 'Voicu', 'florinvoicu', 'florin123'),
(19, 'Carmen', 'Pop', 'carmenpop', 'carmen123'),
(20, 'Adrian', 'Tanase', 'adriantanase', 'adrian123'),
(21, 'Laura', 'Gheorghe', 'lauragheorghe', 'laura123');

INSERT INTO Friendships VALUES
(1, 2, '2023-01-15 10:30:00', 'ACCEPTED'),
(1, 3, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 4, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 7, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 8, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 9, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 10, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 11, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 12, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 13, '2023-02-18 15:45:00', 'ACCEPTED'),
(1, 14, '2023-02-18 15:45:00', 'ACCEPTED'),
(2, 3, '2023-03-20 08:00:00', 'ACCEPTED'),
(2, 4, '2023-04-25 12:15:00', 'ACCEPTED'),
(3, 4, '2023-05-10 09:30:00', 'ACCEPTED'),
(4, 5, '2023-06-05 14:00:00', 'ACCEPTED'),
(5, 6, '2023-07-12 16:20:00', 'ACCEPTED'),
(1, 6, '2023-08-22 11:50:00', 'ACCEPTED'),
(3, 5, '2023-09-15 17:30:00', 'ACCEPTED'),
(2, 6, '2023-10-01 19:45:00', 'ACCEPTED');

INSERT INTO Messages(from_user, to_users, message, date) VALUES
(1, '{2, 3}', 'salut', '2023-01-16 10:30:00');

SELECT * FROM Users;
SELECT * FROM Friendships;
SELECT * FROM Messages;