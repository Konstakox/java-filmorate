package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class FilmService {
    private static final int MAX_NUMBER_OF_CHARACTERS = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MIN_DURATION_FILM = 0;
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final FilmDao filmDao;
    private final UserService userService;

    public List<Film> findAll() {
        return filmDao.getFilms();
    }

    public Film getFilmById(int id) {
        isExistFilmById(id);
        return filmDao.getFilmById(id);
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmDao.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        isExistFilmById(film.getId());
        genreDao.delete(film.getId());
        List<Genre> filmGenres = genreDao.add(film.getId(), film.getGenres());
        film.setGenres(filmGenres);
        return filmDao.updateFilm(film);
    }

    public void addLike(Integer id, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new IncorrectIdException("No ID");
        }
        filmDao.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new IncorrectIdException("No ID");
        }
        filmDao.deleteLike(id, userId);
    }

    public List<Film> sortingByMaxLikes(Integer count) {
        return filmDao.sortingByMaxLikes(count);
    }

    public void validate(Film film) {
        if (film.getDescription().length() > MAX_NUMBER_OF_CHARACTERS) {
            log.debug("Описание более " + MAX_NUMBER_OF_CHARACTERS + " символов");
            throw new ValidationException("Превышено максимальное количество символов: " + MAX_NUMBER_OF_CHARACTERS);
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.debug("Дата релиза фильма раньше " + MIN_RELEASE_DATE);
            throw new ValidationException("Дата релиза фильма раньше " + MIN_RELEASE_DATE);
        }
        if (film.getDuration() <= MIN_DURATION_FILM) {
            log.debug("Продолжительность фильма в минутах должна быть больше: " + MIN_DURATION_FILM);
            throw new ValidationException("Продолжительность фильма в минутах должна быть больше: " + MIN_DURATION_FILM);
        }
    }

    private void isExistFilmById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM films WHERE film_id = ?", id);
        if (!userRows.next()) {
            throw new ObjectNotFoundException("Нет фильма с id: " + id);
        }
    }
}
