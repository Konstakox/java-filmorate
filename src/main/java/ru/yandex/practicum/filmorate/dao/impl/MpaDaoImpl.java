package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Override
    public Mpa get(int mpaId) {
        String sql = "SELECT* FROM mpa WHERE MPA_ID=?";
        return jdbcTemplate.queryForObject(sql, mpaMapper, mpaId);
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT* FROM mpa";
        return jdbcTemplate.query(sql, mpaMapper);
    }

    @Override
    public Map<Integer, Mpa> getAllAsMap() {
        List<Mpa> mpaList = getAll();
        Map<Integer, Mpa> map = new HashMap<>();
        for (Mpa mpa : mpaList) {
            map.put(mpa.getId(), new Mpa(mpa.getId(), mpa.getName()));
        }

        return map;
    }

}