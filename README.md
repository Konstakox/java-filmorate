<h2>java-filmorate</h2>
<p>Учебный проект.
Использовал java 11, Maven, Spring BOOT, JdbcTemplate, H2.
</p>
<h3>Описание</h3>
<p>Приложение - картотека фильмов с возможностью отмечать понравившийся фильм.</p>
<h3>Эндпоинты</h3>
<p><ul>
<li>[POST] /users - создать нового пользователя;</li>
<li>[PUT] /users - обновление существующего пользователя;</li>
<li>[PUT] /users/{id}/friends/{friendId} - "подружить" пользователей;</li>
<li>[GET] /users - получить список всех пользователей;</li>
<li>[GET] /users/{id} - получить пользователя по id;</li>
<li>[GET] /users/{id}/friends/common/{otherId} - получить всех друзей пользователя;</li>
<li>[DELETE] /users/{id} - удалить пользователя по id;</li>
<li>[DELETE] /users/{id}/friends/{friendId} - "разорвать дружбу" пользователей;</li>
</ul></p>
<p><ul>
<li>[POST] /films - создать новый фильм</li>
<li>[PUT] /films - обновление существующего фильма</li>
<li>[PUT] /films/{id}/like/{userId} - поставить лайк фильму</li>
<li>[GET] /films - получить список всех фильмов</li>
<li>[GET] /films/{id} - получить фильм по id</li>
<li>[GET] /films/popular?count={count} - получить список фильмов с сортировкой по их популярности</li>
<li>[DELETE] /films/{id} - удалить фильм по id</li>
<li>[DELETE] /films/{id}/like/{userId} - снять лайк с фильма</li>
</ul></p>

<h3>Схема базы данных</h3>
<p><img src="https://github.com/Konstakox/java-filmorate/blob/main/vol1.png"/></p>

<!--
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
-->
