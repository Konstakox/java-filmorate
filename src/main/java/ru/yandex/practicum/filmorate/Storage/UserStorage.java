package ru.yandex.practicum.filmorate.Storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public List<User> getUsers() {
        if(users.size() == 0) {
            System.out.println("Нет зарегистрированных пользователей.");
        }
        return new ArrayList<>(users.values());
    }

    public void addUser(Integer id, User user) {
        users.put(id, user);
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

public void updateUser(User user) {
        users.remove(user.getId());
        addUser(user.getId(), user);
}
}
