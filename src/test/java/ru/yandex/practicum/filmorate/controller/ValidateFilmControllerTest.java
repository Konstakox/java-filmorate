package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateFilmControllerTest {

    FilmStorage filmStorage;
    FilmController filmController;

    @Test
    void correctFilm() {
        filmStorage = new FilmStorage();
        filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2022, Month.JANUARY, 1))
                .duration(100)
                .build();

        filmController.validate(film);
        filmStorage.addFilm(film.getId(), film);

        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void failDescription() {
        filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                        "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о " +
                        "Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.of(2022, Month.JANUARY, 1))
                .duration(100)
                .build();
        System.out.println(film);

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });

        Assertions.assertEquals("Превышено максимальное количество символов: "
                + FilmController.MAX_NUMBER_OF_CHARACTERS, thrown.getMessage());
    }

    @Test
    void failReleaseDate() {
        filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(100)
                .build();

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });

        Assertions.assertEquals("Дата релиза фильма раньше "
                + FilmController.MIN_RELEASE_DATE, thrown.getMessage());
    }

    @Test
    void failDuration() {
        filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2022, Month.JANUARY, 1))
                .duration(0)
                .build();

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.validate(film);
        });

        Assertions.assertEquals("Продолжительность фильма в минутах должна быть больше: "
                + FilmController.MIN_DURATION_FILM, thrown.getMessage());
    }
}