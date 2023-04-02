package ru.yandex.practicum.filmorate.Storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public List<User> getUsers() {
        if (users.size() == 0) {
            System.out.println("Нет зарегистрированных пользователей.");
        }
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId);
        users.put(nextId++, user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Невозможно обновить данные пользователя, с id: " + user.getId() + " пользователь не найден.");
            throw new ValidationException("Невозможно обновить данные пользователя. Пользователь с id: " + user.getId()
                    + "  не найден.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(int id) {
        return users.get(id);
    }
}
