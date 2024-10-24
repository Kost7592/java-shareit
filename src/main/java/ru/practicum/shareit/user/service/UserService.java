package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User getUserById(Long id);

    UserDto createUser(UserDto newUser);

    UserDto updateUser(UserDto userDto, Long id);

    void deleteUserById(Long id);

    Collection<User> getAllUsers();
}
