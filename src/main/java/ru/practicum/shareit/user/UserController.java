package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * Контроллер UserController для работы с пользователями (users).
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Метод createItem создаёт нового пользователя.
     *
     * @param newUser создаваемый объект нового пользователя.
     * @return созданный пользователь в формате DTO.
     */

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto newUser) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(newUser);
    }

    /**
     * Метод updateUser обновляет данные обновляемого пользователя.
     *
     * @param userDto обновляемый пользователь в формате DTO
     * @param id      идентификатор обновляемого пользователя.
     * @return обновленный пользователь в формате DTO.
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Получен запрос на обновление данных пользователя");
        return userService.updateUser(userDto, id);
    }

    /**
     * Метод getUser получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Получен запрос на получение пользователя");
        return userService.getUserById(id);
    }

    /**
     * Метод deleteUser удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя");
        userService.deleteUserById(id);
    }
}
