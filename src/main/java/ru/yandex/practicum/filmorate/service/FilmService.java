package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserService userService;
    private static final int MAX_NUMBER_OF_CHARACTERS = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MIN_DURATION_FILM = 0;

    public List<Film> findAll() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(Integer id, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new IncorrectIdException("No ID");
        }
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new IncorrectIdException("No ID");
        }
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> sortingByMaxLikes(Integer count) {
        List<Film> poFilm = filmStorage.getFilms()
                .stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(count)
                .collect(Collectors.toList());
        if (poFilm == null) {
            throw new javax.validation.ValidationException("Список фильмов пуст.");
        }
        return poFilm;
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
}
