package ru.yandex.practicum.filmorate.Storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);
}
