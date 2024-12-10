package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Сервис для работы с пользователями.
 */
public interface UserService {
    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    User getUserById(Long id);

    /**
     * Создаёт нового пользователя.
     *
     * @param newUser объект DTO нового пользователя
     * @return объект DTO пользователя
     */
    UserDto createUser(UserDto newUser);

    /**
     * Обновляет существующего пользователя.
     *
     * @param userDto объект DTO обновляемого пользователя
     * @param id      идентификатор обновляемого пользователя
     */
    UserDto updateUser(UserDto userDto, Long id);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор удаляемого пользователя
     */
    void deleteUserById(Long id);

    /**
     * Возвращает всех пользователей.
     *
     * @return коллекция пользователей
     */
    Collection<User> getAllUsers();
}
