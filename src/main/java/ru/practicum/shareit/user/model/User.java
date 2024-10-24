package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс User представляет собой модель пользователя.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор пользователя;
 * - name — имя пользователя;
 * - email — электронная почта;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @Email(message = "Указан некорректный Email")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
}
