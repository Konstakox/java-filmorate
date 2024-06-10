package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Data
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: 'GET /films/{id}'");
        return filmService.getFilmById(id);
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: 'POST /films'");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: 'PUT /films'");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос к эндпоинту: 'PUT /films/{id}/like/{userId}'");
        if (id == null || userId == null) {
            log.info("Отсутствует ID");
        } else {
            filmService.addLike(id, userId);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос к эндпоинту: 'DELETE /films/{id}/like/{userId}'");
        if (id == null || userId == null) {
            log.info("Отсутствует ID");
        } else {
            filmService.deleteLike(id, userId);
        }
    }

    @GetMapping("/popular")
    public List<Film> sortingByMaxLikes(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос к эндпоинту: 'GET /films/popular'");
        if (count <= 0) {
            throw new IncorrectCountException("Для рейтинга нужно количество выбранных фильмов больше 0. Вы указали: "
                    + count);
        }
        return filmService.sortingByMaxLikes(count);
    }
}
