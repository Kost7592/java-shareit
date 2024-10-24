package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @Email(message = "Указан некорректный Email")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
}
