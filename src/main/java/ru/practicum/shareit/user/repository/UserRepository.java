package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Интерфейс UserRepository определяет методы для работы с пользователями.
 */
public interface UserRepository {
    /**
     * Метод getUserById возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, который становится владельцем нового элемента.
     * @return созданный элемент.
     */
    User getUserById(Long id);

    /**
     * Метод createUser создает нового пользователя.
     *
     * @param newUser пользователь, данные которого будут использоваться для создания нового объекта.
     * @return созданный пользователь.
     */
    User createUser(User newUser);

    /**
     * Метод updateUser обновляет существующего пользователя.
     *
     * @param updatedUser обновленный пользователь.
     * @param id          идентификатор обновляемого пользователя.
     * @return обновленный пользователь.
     */
    User updateUser(User updatedUser, Long id);

    /**
     * Метод deleteUserById удаляет.
     *
     * @param id идентификатор удаляемого пользователя.
     */
    void deleteUserById(Long id);

    /**
     * Метод getAllUsers возвращает список всех пользователей.
     *
     * @return список пользователей.
     */
    Collection<User> getAllUsers();
}
