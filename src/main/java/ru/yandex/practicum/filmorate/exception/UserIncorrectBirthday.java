package ru.yandex.practicum.filmorate.exception;

public class UserIncorrectBirthday extends RuntimeException {
    public UserIncorrectBirthday(String s) {
        super(s);
    }
}
