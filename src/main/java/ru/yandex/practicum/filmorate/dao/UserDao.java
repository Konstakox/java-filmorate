package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserDao {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUser(int id);


    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<User> getFriends(Integer id);

    List<User> getMutualFriends(Integer id, Integer otherId);

    Boolean isExistUserById(int id);
}
