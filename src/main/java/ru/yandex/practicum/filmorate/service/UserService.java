package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class UserService {
    @Autowired
    private UserStorage userStorage;

    private final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s");

    public List<User> findAll() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUser(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        userStorage.getUser(id).addFriends(friendId);
        userStorage.getUser(friendId).addFriends(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        userStorage.getUser(id).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(id);
    }

    public List<User> getFriends(Integer id) {
        Set<Integer> friendsId = userStorage.getUser(id).getFriends();
        List<User> friendsUser = new ArrayList<>();
        for (Integer item : friendsId) {
            friendsUser.add(userStorage.getUser(item));
        }
        return friendsUser;
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        List<User> friendsId = getFriends(id);
        List<User> friendsOtherId = getFriends(otherId);
        friendsId.retainAll(friendsOtherId);
        return friendsId;
    }

    public void validate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения указана в будущем времени.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        Matcher matcher = PATTERN_WHITESPACE.matcher(user.getLogin());
        if (matcher.find()) {
            log.debug("Логин содержит пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectUser(final ValidationException e) {
        return Map.of(
                "error", "Некорректные данные пользователя.",
                "error ", e.getMessage()
        );
    }
}
