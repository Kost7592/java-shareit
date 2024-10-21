package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User getUserById(Long id);

    User createUser(User newUser);

    User updateUser(User updatedUser, Long id);

    void deleteUserById(Long id);

    Collection<User> getAllUsers();
}
