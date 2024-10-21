package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto newUser) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(newUser);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Получен запрос на обновление данных пользователя");
        return userService.updateUser(userDto, id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя");
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUserById(id);
    }
}
