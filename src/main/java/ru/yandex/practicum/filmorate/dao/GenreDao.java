package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre get(int id);

    List<Genre> getAll();

    List<Genre> getGenresListForFilm(int filmId);

    List<Genre> add(int filmId, List<Genre> genres);

    void delete(int filmId);
}
