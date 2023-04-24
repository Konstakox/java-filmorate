package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Override
    public Mpa get(int mpaId) {
        String sql = "SELECT* FROM mpa WHERE mpa_id=?";
        return jdbcTemplate.queryForObject(sql, mpaMapper, mpaId);
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT* FROM mpa";
        return jdbcTemplate.query(sql, mpaMapper);
    }
}