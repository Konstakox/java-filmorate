package ru.yandex.practicum.filmorate.Storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FilmStorage {
    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;

    public List<Film> getFilms() {
        if(films.size() == 0) {
            System.out.println("Список фильмов пуст.");
        }
        return new ArrayList<>(films.values());
    }

    public void addFilm(Integer id, Film film) {
        films.put(id, film);
    }

    public void updateFilm(Film film) {
        films.remove(film.getId());
        addFilm(film.getId(), film);
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
