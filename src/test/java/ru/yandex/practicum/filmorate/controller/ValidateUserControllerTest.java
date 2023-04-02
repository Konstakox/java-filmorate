package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
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
}