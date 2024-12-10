package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс UserDto представляет собой DTO (Data Transfer Object) для пользователя.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор пользователя;
 * - name — имя пользователя;
 * - email — электронная почта;
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
