package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс CommentDtoResponse представляет собой DTO (Data Transfer Object) для получения комментария через ответ на
 * запрос.
 * Он содержит следующие поля:
 * - id - уникальный идентификатор комментария;
 * - text — текст комментария;
 * - authorName - имя автора комментария;
 * - created - дата и время добавления комментария.
 */
@Data
@AllArgsConstructor
public class CommentDtoResponse {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
