package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс CommentDtoRequest представляет собой DTO (Data Transfer Object) для передачи комментария через запрос.
 * Он содержит следующее поле:
 * - text — текст комментария;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    @NotBlank
    @Size(max = 512)
    private String text;
}

