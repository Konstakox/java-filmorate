package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage = new FilmStorage();

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (validate(film)) {
            int id = filmStorage.getNextId();
            film.setId(id);
            filmStorage.setNextId(film.getId() + 1);
            log.info("Счетчик id увеличился, следующий номер: " + filmStorage.getNextId());
            filmStorage.addFilm(film.getId(), film);
            log.info("Создан объект фильм: " + film);
        }
        return film;
    }

//    @PutMapping
//    public Film put(@Valid @RequestBody Film film) {
//        if(filmStorage.getFilms().contains(film)) {
//            if(validate(film)) {
//                filmStorage.updateFilm(film);
//                log.info("Обновлён объект фильм: " + film);
//            }
//        } else {
//            createFilm(film);
//        }
//        return film;
//    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if(!filmStorage.getFilms().contains(film)) {
            log.debug("Невозможно обновить данные фильма, с id: " + film.getId() + " пользователь не найден.");
            throw new ValidationException("Невозможно обновить данные фильма. Фильма с id: " + film.getId()
                    + "  не найден.");
        } else {
            if(validate(film)) {
                filmStorage.updateFilm(film);
                log.info("Обновлён объект фильм: " + film);
            }
        }
        return film;
    }


    private boolean validate(Film film) {
        if (film.getDescription().length() > 200) {
            log.debug("Описание более 200 символов");
            throw new ValidationException("Превышена максимальная длина описания в 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза фильма раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.debug("Отрицательная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }
}
