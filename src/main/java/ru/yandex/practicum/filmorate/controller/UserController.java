package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Data
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: 'GET /users/{id}'");
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос к эндпоинту: 'PUT /users/{id}/friends/{friendId}'");
        if (id == null || friendId == null) {
            throw new IncorrectIdException("Fail addFriend. Нет пользователя с id: " + id + " или " + friendId);
        }
        if (id < 0 || friendId < 0) {
            throw new IncorrectIdException("Fail addFriend. Отрицательный id: " + id + " или " + friendId);
        } else {
            userService.addFriend(id, friendId);
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос к эндпоинту: 'DELETE /users/{id}/friends/{friendId}'");
        if (id == null || friendId == null) {
            throw new IncorrectIdException("Fail deleteFriend. Не введен id: ");
        } else {
            userService.deleteFriend(id, friendId);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: 'GET /users/{id}/friends'");
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос к эндпоинту: 'GET /users/{id}/friends/common/{otherId}'");
        return userService.getMutualFriends(id, otherId);
    }
}
