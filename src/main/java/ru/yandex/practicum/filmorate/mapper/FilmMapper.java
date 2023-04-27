package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
        film.setGenres(genres);

        return film;
    }
}
