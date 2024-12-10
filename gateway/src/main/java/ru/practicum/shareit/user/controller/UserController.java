package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.validation.Create;

/**
 * Класс представляет собой контроллер для работы с пользователями (users) в системе.
 */
@Controller("GatewayUserController")
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    static final String path = "/{user-id}";
    private final UserClient userClient;

    /**
     * Метод addUser создаёт нового пользователя.
     *
     * @param userDto создаваемый объект нового пользователя.
     * @return ответ в формате ResponseEntity.
     */
    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated({Create.class}) UserDtoRequest userDto) {
        log.info("POST запрос на создание пользователя {}", userDto);
        return userClient.postUser(userDto);
    }

    /**
     * Метод getUser получает пользователя по идентификатору.
     * @param userId идентификатор пользователя.
     * @return ответ в формате ResponseEntity.
     */
    @GetMapping(path)
    public ResponseEntity<Object> getUser(@PathVariable("user-id") Long userId) {
        log.info("GET запрос на получение пользователя userId={}", userId);
        return userClient.getUser(userId);
    }

    /**
     * Метод для получения всех пользователей
     * @return ответ в формате ResponseEntity.
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET запрос на получение всех пользователей");
        return userClient.getAllUsers();
    }

    /**
     * Метод updateUser обновляет данные обновляемого пользователя.
     * @param userDto обновляемый пользователь в формате DTO
     * @param userId идентификатор обновляемого пользователя.
     * @return ответ в формате ResponseEntity.
     */
    @PatchMapping(path)
    public ResponseEntity<Object> updateUser(@RequestBody UserDtoRequest userDto, @PathVariable("user-id") long userId) {
        log.info("PATCH запрос на обновление пользователя userId={}, userDto={}", userId, userDto);
        return userClient.patchUser(userDto, userId);
    }

    /**
     * Метод deleteUser удаляет пользователя по его идентификатору.
     * @param userId id идентификатор пользователя.
     * @return ответ в формате ResponseEntity.
     */
    @DeleteMapping(path)
    public ResponseEntity<Object> deleteUser(@PathVariable("user-id") long userId) {
        log.info("DELETE запрос на удаление пользователя userId={}", userId);
        return userClient.delete(userId);
    }
}
