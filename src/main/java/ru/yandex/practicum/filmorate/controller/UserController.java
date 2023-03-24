package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserIncorrectBirthday;
import ru.yandex.practicum.filmorate.exception.UserLoginWithoutSpace;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage = new UserStorage();

    @GetMapping
    public List<User> findAll() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (validate(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            int id = userStorage.getNextId();
            user.setId(id);
            userStorage.setNextId(user.getId() + 1);
            log.info("Счетчик id увеличился, следующий номер: " + userStorage.getNextId());
            userStorage.addUser(user.getId(), user);
            log.info("Создан объект пользователь: " + user);
        }
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!userStorage.getUsers().contains(user)) {
            log.debug("Невозможно обновить данные пользователя, с id: " + user.getId() + " пользователь не найден.");
            throw new ValidationException("Невозможно обновить данные пользователя. Пользователь с id: " + user.getId()
                    + "  не найден.");
        } else {
            if (validate(user)) {
                userStorage.updateUser(user);
                log.info("Обновлён объект пользователь: " + user);
            }
        }
        return user;
    }

    private boolean validate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения указана в будущем времени.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        String login = user.getLogin();
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(login);
        if (matcher.find()) {
            log.debug("Логин содержит пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        return true;
    }
}
