package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaDao;
    private final JdbcTemplate jdbcTemplate;

    public Mpa get(int id) {
        if (!isExist(id)) {
            throw new ObjectNotFoundException("Рейтинг не найден");
        }
        return mpaDao.get(id);
    }

    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }

    private boolean isExist(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM mpa WHERE mpa_id = ?", id);
        return userRows.next();
    }
}