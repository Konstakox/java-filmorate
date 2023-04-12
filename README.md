# java-filmorate
Template repository for Filmorate project.
![Схема](/vol1.png)

Таблицы с Данными

films список фильмов
MPA рейтинг Ассоциации кинокомпаний
film_genres связь фильмов с жанрами
genre список жанров
userLikes лайки пользователей
users список пользователей
friends связи дружбы между пользователями

Примеры запросов

Список фильмов в жанре "ACTION"

SELECT f.name
FROM films AS f
LEFT JOIN film_genres ON f.film_id = film_genres.film_id
WHERE f.genre LIKE '%ACTION%'

Список фильмов с рейтингом строго 18+

SELECT f.name 
FROM films AS f
WHERE f.mpa='NC17'