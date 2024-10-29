package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
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
     * Метод toUser преобразует объект UserDto в объект User.
     */
    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}