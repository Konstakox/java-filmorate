package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NonNull
public class Film {
    private int id;                     //целочисленный идентификатор
    @NotBlank
    private String name;                //название
    private String description;         //описание
    private LocalDate releaseDate;      //дата релиза
    private int duration;               //продолжительность фильма

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
