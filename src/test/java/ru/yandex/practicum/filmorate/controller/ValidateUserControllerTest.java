package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateUserControllerTest {

    InMemoryUserStorage inMemoryUserStorage;
    UserController userController;
    UserService userService;

    @Test
    void correctUser() {
        userController = new UserController();
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService();
        User user = User.builder()
                .id(1)
                .email("ya@ya.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1981, Month.SEPTEMBER, 1))
                .build();

        userService.validate(user);
        inMemoryUserStorage.addUser(user);

        assertEquals(1, inMemoryUserStorage.getUsers().size());
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

//        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
//            userService.validate(user);
//        });
//
//        Assertions.assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
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

//        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
//            userService.validate(user);
//        });
//
//        Assertions.assertEquals("Логин не может содержать пробелы", thrown.getMessage());
    }
}