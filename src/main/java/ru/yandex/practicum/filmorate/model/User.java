package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NonNull
public class User {
    private int id;                 //целочисленный идентификатор
    @Email
    private String email;           //электронная почта
    @NotBlank
    private String login;           //логин пользователя
    private String name;            //имя для отображения
    private LocalDate birthday;     //дата рождения

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
