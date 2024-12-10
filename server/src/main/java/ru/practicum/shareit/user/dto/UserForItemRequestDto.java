package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс UserForItemRequestDto представляет собой DTO (Data Transfer Object) для пользователя.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор пользователя;
 * - name — имя пользователя;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForItemRequestDto {
    private Long id;
    private String name;
}