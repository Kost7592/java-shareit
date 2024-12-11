package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляет собой DTO (Data Transfer Object) для запроса на комментарий.
 * Содержит следующие поля
 * - text - текст комментария.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDtoRequest {
    @NotBlank
    @Size(max = 512)
    private String text;
}