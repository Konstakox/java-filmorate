package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

public interface MpaDao {
    Mpa get(int mpaId);

    List<Mpa> getAll();

    Map<Integer, Mpa> getAllAsMap();
}
