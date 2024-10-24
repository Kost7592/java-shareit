package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserDto createUser(UserDto newUser) {
        User user = UserMapper.toUser(newUser);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User updatedUser = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.updateUser(updatedUser, id));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
