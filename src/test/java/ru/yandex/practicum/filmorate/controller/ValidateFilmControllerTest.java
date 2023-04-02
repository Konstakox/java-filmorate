package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateFilmControllerTest {

    InMemoryFilmStorage inMemoryFilmStorage;
    FilmController filmController;
    FilmService filmService;

    @Test
    void correctFilm() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        filmController = new FilmController();
        filmService = new FilmService();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2022, Month.JANUARY, 1))
                .duration(100)
                .build();

        filmService.validate(film);
        inMemoryFilmStorage.addFilm(film);

        assertEquals(1, inMemoryFilmStorage.getFilms().size());
    }
}