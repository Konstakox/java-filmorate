package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Storage.film.FilmStorage;
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
    public final int maxNumberOfCharacters = 200;
    public final LocalDate minReleaseDate = LocalDate.of(1895, Month.DECEMBER, 28);
    public final int minDurationFilm = 0;

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
        if (film.getDescription().length() > maxNumberOfCharacters) {
            log.debug("Описание более " + maxNumberOfCharacters + " символов");
            throw new ValidationException("Превышено максимальное количество символов: " + maxNumberOfCharacters);
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.debug("Дата релиза фильма раньше " + minReleaseDate);
            throw new ValidationException("Дата релиза фильма раньше " + minReleaseDate);
        }
        if (film.getDuration() <= minDurationFilm) {
            log.debug("Продолжительность фильма в минутах должна быть больше: " + minDurationFilm);
            throw new ValidationException("Продолжительность фильма в минутах должна быть больше: " + minDurationFilm);
        }
    }
}
