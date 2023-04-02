package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class IncorrectIdException extends RuntimeException{
    public IncorrectIdException(String message) {
        super(message);
    }
}
