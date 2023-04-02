package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int nextId = 1;

    public List<Film> getFilms() {
        if (films.size() == 0) {
            System.out.println("Список фильмов пуст.");
        }
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film) {
        film.setId(nextId);
        films.put(nextId++, film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Fail updateFilm, no id: " + film.getId());
            throw new ValidationException("Fail updateFilm. Нет фильма с id: " + film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }

    public void addLike(Integer id, Integer userId) {
        if (getFilmById(id).addLike(userId)) {
            getFilmById(id).setLike(getFilmById(id).getLike() + 1);
        }
        log.debug("Fail addLike, лайк уже существует с id user : " + userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (getFilmById(id).deleteLike(userId)) {
            getFilmById(id).setLike(getFilmById(id).getLike() - 1);
        }
        log.debug("Fail deleteLike, лайк отсутствует с id user : " + userId);
    }
}
