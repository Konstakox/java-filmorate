package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class UserService {
    private static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s");
    @Autowired
    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userDao.getUsers();
    }

    public User addUser(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userDao.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        isExistUserById(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userDao.updateUser(user);
    }

    public User getUserById(int id) {
        isExistUserById(id);
        return userDao.getUser(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        isExistUserById(id);
        isExistUserById(friendId);
        userDao.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        isExistUserById(id);
        isExistUserById(friendId);
        userDao.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        isExistUserById(id);
        return userDao.getFriends(id);
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        isExistUserById(id);
        isExistUserById(otherId);
        return userDao.getMutualFriends(id, otherId);
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
        if (!userDao.isExistUserById(id)) {
            throw new UserNotFoundException("Нет пользователя с id: " + id);
        }
    }

}
