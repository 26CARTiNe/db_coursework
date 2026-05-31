INSERT INTO cities (id, country, name)
VALUES (1, 'Россия', 'Москва'),
       (2, 'Россия', 'Санкт-Петербург'),
       (3, 'Германия', 'Мюнхен'),
       (4, 'Испания', 'Барселона'),
       (5, 'Англия', 'Лондон');

-- Заполнение teams (используем ID из cities)
INSERT INTO teams (id, city_id, name, peoplesInTeam, numOfWin)
VALUES (1, 1, 'Спартак', 25, 15),
       (2, 1, 'ЦСКА', 26, 18),
       (3, 2, 'Зенит', 24, 20),
       (4, 3, 'Бавария', 27, 22),
       (5, 4, 'Барселона', 28, 25),
       (6, 5, 'Арсенал', 26, 19);

-- Заполнение referees
INSERT INTO referees (id, city_id, fio, license, stageYears)
VALUES (1, 1, 'Иванов Иван Иванович', 'REF123', 10),
       (2, 2, 'Петров Пётр Петрович', 'REF456', 8),
       (3, 3, 'Шмидт Ганс', 'REF789', 12),
       (4, 4, 'Гарсия Хуан', 'REF012', 9),
       (5, 5, 'Смит Джон', 'REF345', 11);

-- Заполнение matches
INSERT INTO matches (team_guest_id, team_host_id, referee_id, city_id, stageType, phaseType, guestCount, hostCount,
                     dateTime)
VALUES (2, 1, 1, 1, 1, 4, 0, 0, '2023-10-15 15:00:00'),
       (3, 4, 3, 3, 2, 4, 0, 0,'2023-10-16 18:00:00'),
       (5, 6, 5, 5, 3, 3, 0, 0,'2023-10-17 20:00:00'),
       (1, 3, 2, 2, 4, 2, 0, 0,'2023-10-18 16:00:00'),
       (4, 5, 4, 4, 6, 1, 0, 0,'2023-10-19 19:00:00');

DELETE FROM users WHERE login = 'admin';

INSERT INTO users (login, password, role)
VALUES ('admin', '$2a$10$XKvHJZ8Z9Z9Z9Z9Z9Z9Z9O8xKvHJZ8Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z9Z', 'ADMIN');