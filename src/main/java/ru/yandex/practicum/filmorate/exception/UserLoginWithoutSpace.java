package ru.yandex.practicum.filmorate.exception;

public class UserLoginWithoutSpace extends Exception{
    public UserLoginWithoutSpace(String s) {
        super(s);
    }
}
