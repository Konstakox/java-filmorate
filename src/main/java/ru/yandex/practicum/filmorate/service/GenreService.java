package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public Genre get(int id) {
        if (!isExist(id)) {
            throw new ObjectNotFoundException("Жанр не найден");
        }
        return genreDao.get(id);
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    private boolean isExist(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM genres WHERE genre_id = ?", id);
        return userRows.next();
    }
}