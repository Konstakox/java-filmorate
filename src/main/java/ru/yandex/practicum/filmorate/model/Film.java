package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Film {
    private int id;                     //целочисленный идентификатор
    @NotBlank
    private String name;                //название film_name
    private String description;         //описание
    private LocalDate releaseDate;      //дата релиза
    private int duration;               //продолжительность фильма
    private Mpa mpa;                 //возрастное ограничение для фильма
    private List<Genre> genres;                            //жанр

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
