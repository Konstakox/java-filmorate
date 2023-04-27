package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT* FROM films";
        return jdbcTemplate.query(sqlQuery, filmMapper);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setObject(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        saveGenresToFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ? , MPA_ID = ?" +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteGenres(film.getId());
        saveGenresToFilm(film);
        return film;
    }

    private void saveGenresToFilm(Film film) {
        final Integer filmId = film.getId();
        final List<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final ArrayList<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(
                "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genreList.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genreList.size();
                    }
                });
    }

    public void deleteGenres(int filmID) {
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID);
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT f.FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, f.FILM_ID, g.GENRE_ID, g.GENRE_NAME, m.MPA_ID, m.MPA_NAME " +
                "FROM films f " +
                "LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE f.film_id=?";

        List<Film> films = jdbcTemplate.query(sqlQuery, filmMapper, id);

        if (!films.isEmpty()) {
            return setGenresToFilm(films).get(0);
        } else {
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        String sqlQuery = "INSERT INTO movie_likes(user_id, film_id) VALUES(?,?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        String sql = "DELETE FROM movie_likes WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sql, userId, id);
    }

    @Override
    public List<Film> sortingByMaxLikes(Integer count) {
        String sql = "WITH cte AS " +
                "(SELECT f.FILM_ID, COUNT(user_id) rate " +
                "FROM FILMS f " +
                "LEFT JOIN MOVIE_LIKES ml ON f.FILM_ID = ml.FILM_ID GROUP BY f.FILM_ID ORDER BY rate DESC LIMIT ?) " +
                "SELECT f.FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, cte.rate, f.FILM_ID, " +
                "m.MPA_ID, m.MPA_NAME, g.GENRE_ID, g.GENRE_NAME, l.USER_ID " +
                "FROM FILMS f " +
                "LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID " +
                "LEFT JOIN MOVIE_LIKES l ON f.FILM_ID = l.FILM_ID " +
                "LEFT JOIN cte ON f.FILM_ID = cte.FILM_ID " +
                "WHERE f.FILM_ID IN (SELECT FILM_ID FROM cte) " +
                "ORDER BY rate DESC;";

        List<Film> filmList = new ArrayList<>(jdbcTemplate.query(sql, filmMapper, count));

        return setGenresToFilm(filmList);
    }

    private List<Film> setGenresToFilm(List<Film> filmList) {
        List<Film> fixedFilmList = new ArrayList<>();

        for (Film film : filmList) {
            if (!fixedFilmList.contains(film)) {
                fixedFilmList.add(film);
            } else {
                Film existedFilm = fixedFilmList.get(fixedFilmList.indexOf(film));
                if (!film.getGenres().isEmpty()) {
                    existedFilm.getGenres().add(film.getGenres().get(0));
                }
            }
        }
        return fixedFilmList;
    }
}
