package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.model.User;

/**
 * Класс UserMapper содержит методы для преобразования объектов между DTO и моделью пользователя.
 */
public class UserMapper {
    /**
     * Метод toUserDto преобразует объект User в объект UserDto.
     */
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    /**
     * Метод toUserDto преобразует объект User в объект UserWithIdDto.
     */
    public static UserWithIdDto toUserWithIdDto(User user) {
        return new UserWithIdDto(user.getId());
    }

    /**
     * Метод toUser преобразует объект UserDto в объект User.
     */
    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}