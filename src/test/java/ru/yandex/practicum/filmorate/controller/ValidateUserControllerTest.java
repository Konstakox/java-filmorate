package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateUserControllerTest {

    UserStorage userStorage;
    UserController userController;

    @Test
    void correctUser() {
        userController = new UserController();
        userStorage = new UserStorage();
        User user = User.builder()
                .id(1)
                .email("ya@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1981, Month.SEPTEMBER, 1))
                .build();

        userController.validate(user);
        userStorage.addUser(user.getId(), user);

        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    void failBirthday() {
        userController = new UserController();
        User user = User.builder()
                .id(1)
                .email("ya@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2081, Month.SEPTEMBER, 1))
                .build();

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.validate(user);
        });

        Assertions.assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
    }

    @Test
    void failLogin() {
        userController = new UserController();
        User user = User.builder()
                .id(1)
                .email("ya@ya.ru")
                .login("log in")
                .name("name")
                .birthday(LocalDate.of(1981, Month.SEPTEMBER, 1))
                .build();

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.validate(user);
        });

        Assertions.assertEquals("Логин не может содержать пробелы", thrown.getMessage());
    }
}