package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
//@RequiredArgsConstructor
@Data
@RequestMapping("/users")
public class UserController {
@Autowired
    private UserService userService;

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
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (optionalUser.isEmpty()) {
            throw new IncorrectIdException("Fail getUserId. Нет пользователя с id: " + id);
        }

        return userService.getUserById(id);
    };

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null || friendId == null) {
            throw new IncorrectIdException("Fail addFriend. Нет пользователя с id: " + id + " или " +friendId);
        }
        if (id < 0 || friendId < 0) {
            throw new IncorrectIdException("Fail addFriend. Отрицательный id: " + id + " или " +friendId);
        }
        else {
            userService.addFriend(id, friendId);
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null || friendId == null) {
            throw new IncorrectIdException("Fail deleteFriend. Не введен id: ");
        } else {
            userService.deleteFriend(id, friendId);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    };

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    };

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleIncorrectId(final IncorrectIdException e) {
        return Map.of(
                "error", "Передан некорректный Id.",
                "error ", e.getMessage()
        );
    }
}
