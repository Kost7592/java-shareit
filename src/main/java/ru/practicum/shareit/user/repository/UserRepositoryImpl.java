package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long idCount = 1;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получена вся коллекция пользователей.");
        return users.values();
    }

    @Override
    public User createUser(User newUser) {
        mailDuplicateCheck(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Новый пользователь: {} создан", newUser.getName());
        return newUser;
    }

    @Override
    public User updateUser(User updatedUser, Long id) {
        if (users.containsKey(id)) {
            mailDuplicateCheck(updatedUser);
            updateUserFields(updatedUser, id);
            updatedUser.setId(id);
            log.info("Информация о пользователе {} обновлена", updatedUser.getName());
            return updatedUser;
        }
        log.error("Пользователь с таким id: {} + не найден!", updatedUser.getId());
        throw new NotFoundException("Пользователь с таким id: " + updatedUser.getId() + " не найден!");
    }

    @Override
    public void deleteUserById(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            log.error("Пользователь для удаления с id {} не существует", id);
            throw new NotFoundException("Пользователь с id " + id + " не существует");
        }
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с id {} не существует", id);
            throw new NotFoundException("Пользователь с id " + id + " не существует");
        }
        return users.get(id);
    }

    /**
     * Метод updateUserFields обновляет поля обновляемого пользователя.
     *
     * @param updatedUser обновляемый пользователь.
     * @param id          идентификатор обновляемого пользователя.
     */
    private void updateUserFields(User updatedUser, Long id) {
        User user = users.get(id);
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        users.put(id, user);
    }

    /**
     * Метод mailDuplicateCheck проверяет дублирование поля email проверяемого пользователя с аналогичным полем других
     * пользователей.
     *
     * @param checkedUser проверяемый пользователь.
     */
    private void mailDuplicateCheck(User checkedUser) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(checkedUser.getEmail()))) {
            log.error("@mail: {} используется другим пользователем", checkedUser.getEmail());
            throw new DuplicateException("Такой @mail уже занят");
        }
    }

    /**
     * Метод getNextId возвращает следующий идентификатор пользователя.
     *
     * @return следующий идентификатор.
     */
    private long getNextId() {
        return idCount++;
    }
}
