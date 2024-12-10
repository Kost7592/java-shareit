package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс ItemSearchOfTextDto представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями
 * приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSearchOfTextDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}