package ru.yandex.practicum.filmorate.Storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUser(int id);
}
