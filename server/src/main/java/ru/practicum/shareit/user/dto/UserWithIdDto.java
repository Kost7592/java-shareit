package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * Класс UserWithIdDto представляет собой DTO (Data Transfer Object) для пользователя.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор пользователя;
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithIdDto {
    private Long id;
}