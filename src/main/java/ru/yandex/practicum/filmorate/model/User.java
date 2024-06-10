package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
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
public class User {
    private int id;                 //целочисленный идентификатор
    @Email
    private String email;           //электронная почта
    @NotBlank
    private String login;           //логин пользователя
    private String name;            //имя для отображения
    private LocalDate birthday;     //дата рождения
    private Set<Integer> friends = new HashSet<>();      //id друзей

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }

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
