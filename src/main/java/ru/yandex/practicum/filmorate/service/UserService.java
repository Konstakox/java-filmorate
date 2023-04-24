package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class UserService {
    private static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s");
    @Autowired
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        isExistUserById(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        isExistUserById(id);
        return userStorage.getUser(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        isExistUserById(id);
        isExistUserById(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        isExistUserById(id);
        isExistUserById(friendId);
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        isExistUserById(id);
        return userStorage.getFriends(id);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        isExistUserById(id);
        isExistUserById(otherId);
        return userStorage.getMutualFriends(id, otherId);
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

    private void isExistUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT* FROM users WHERE user_id = ?", id);
        if (!userRows.next()) {
            throw new UserNotFoundException("Нет пользователя с id: " + id);
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
