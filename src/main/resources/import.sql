INSERT INTO cities (country, name) VALUES 
('Россия', 'Москва'),
('Россия', 'Санкт-Петербург'),
('Германия', 'Мюнхен'),
('Испания', 'Барселона'),
('Англия', 'Лондон');

INSERT INTO teams (city_id, name, peoplesInTeam, numOfWin) VALUES 
(1, 'Спартак', 25, 15),
(1, 'ЦСКА', 26, 18),
(2, 'Зенит', 24, 20),
(3, 'Бавария', 27, 22),
(4, 'Барселона', 28, 25),
(5, 'Арсенал', 26, 19);

INSERT INTO referees (city_id, fio, license, stageYears) VALUES 
(1, 'Иванов Иван Иванович', 'REF123', 10),
(2, 'Петров Пётр Петрович', 'REF456', 8),
(3, 'Шмидт Ганс', 'REF789', 12),
(4, 'Гарсия Хуан', 'REF012', 9),
(5, 'Смит Джон', 'REF345', 11);

INSERT INTO matches (team_guest_id, team_host_id, referee_id, city_id, stageType, phaseType, guestCount, hostCount, dateTime) VALUES 
(2, 1, 1, 1, 1, 4, 0, 0, '2023-10-15 15:00:00'),
(3, 4, 3, 3, 2, 4, 0, 0, '2023-10-16 18:00:00'),
(5, 6, 5, 5, 3, 3, 0, 0, '2023-10-17 20:00:00'),
(1, 3, 2, 2, 4, 2, 0, 0, '2023-10-18 16:00:00'),
(4, 5, 4, 4, 6, 1, 0, 0, '2023-10-19 19:00:00');

DELETE FROM users WHERE login = 'admin';

INSERT INTO users (id, login, password, role)
VALUES (1, 'admin', '$2a$10$8.mQI4AwkoaNvq0CP3gmS.kirU6F72CQ0Veevea5G9HHSEJB95SlS', 'ADMIN');
