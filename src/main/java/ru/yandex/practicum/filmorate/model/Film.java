package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NonNull
public class Film {
    private int id;                     //целочисленный идентификатор
    @NotBlank
    private String name;                //название
    private String description;         //описание
    private LocalDate releaseDate;      //дата релиза
    private int duration;               //продолжительность фильма
    private int like;                   //количество лайков, в задании определено как рейтинг
    private Set<Integer> userLike = new HashSet<>();      //id кто поставил лайк

    public boolean addLike(Integer userId) {
        return userLike.add(userId);
    }

    public boolean deleteLike(Integer userId) {
        return userLike.remove(userId);
    }


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
