package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Storage.FilmStorage;
import ru.yandex.practicum.filmorate.Storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmController filmController;
    UserController userController;
    ValidationException validationException;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmStorage();
        userStorage = new UserStorage();
        filmController = new FilmController();
        userController = new UserController();
    }

    @Test
    void findAll() {
        User user = User.builder()
                .email("ya@ya.ru")
                .login("login")
                .birthday(LocalDate.of(1981, Month.JANUARY, 1))
                .build();

        userController.createUser(user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void createUserFailEmail() {
        User user = User.builder()
                .email("yaya.ru")
                .login("login")
                .birthday(LocalDate.of(1981, Month.JANUARY, 1))
                .build();

//        userController.createUser(user);
//        assertEquals("не учили проверять аннотации",  "отвечает за это поле @Email");
    }

    @Test
    void createUserFailBirthday() {
        User user = User.builder()
                .email("ya@ya.ru")
                .login("login")
                .birthday(LocalDate.of(2081, Month.JANUARY, 1))
                .build();

//        userController.createUser(user);
//        assertEquals("Дата рождения не может быть в будущем.",  validationException.getMessage());
    }


    @Test
    void put() {
    }
}