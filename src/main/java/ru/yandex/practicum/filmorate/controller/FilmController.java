package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public final static int MAX_NUMBER_OF_CHARACTERS = 200;
    public final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    public final static int MIN_DURATION_FILM = 0;
    private final FilmStorage filmStorage = new FilmStorage();

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validate(film);
        int id = filmStorage.getNextId();
        film.setId(id);
        filmStorage.setNextId(film.getId() + 1);
        log.info("Счетчик id увеличился, следующий номер: " + filmStorage.getNextId());
        filmStorage.addFilm(film.getId(), film);
        log.info("Создан объект фильм: " + film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (!filmStorage.getFilms().contains(film)) {
            log.debug("Невозможно обновить данные фильма, с id: " + film.getId() + " пользователь не найден.");
            throw new ValidationException("Невозможно обновить данные фильма. Фильма с id: " + film.getId()
                    + "  не найден.");
        }
        validate(film);
        filmStorage.updateFilm(film);
        log.info("Обновлён объект фильм: " + film);

        return film;
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
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Продолжительность фильма в минутах должна быть больше: " + MIN_DURATION_FILM);
        }
    }
}
