# java-filmorate
Template repository for Filmorate project.
Проект реализован в рамках обучения яндекс-практикум.
Бэкэнд сервиса по подбору фильмов для просмотра.
![Схема](/PUBLIC.png)

Таблицы с Данными

films список фильмов
MPA рейтинг Ассоциации кинокомпаний
film_genre связь фильмов с жанрами
genres список жанров
movie_likes лайки пользователей
users список пользователей
friends связи дружбы между пользователями

Примеры запросов

Список фильмов в жанре "Боевик"

SELECT f.name, g.name
FROM films AS f
LEFT JOIN film_genre ON f.film_id = film_genres.film_id
WHERE f.genre LIKE '%Боевик%'

Список друзей пользователя с id = 1

SELECT * FROM users WHERE user_id IN
( SELECT friend_id FROM friends WHERE user_id = 1 );

Возвращаем Топ 10 популярных фильмов

SELECT f.film_id, film_name, description, duration, release_date, mpa_id, count\n
FROM (SELECT film_id ,COUNT(user_id) AS count
FROM movie_likes AS ml
GROUP BY film_id) AS likes
RIGHT JOIN films AS f ON f.film_id=likes.film_id
ORDER BY count LIMIT 10