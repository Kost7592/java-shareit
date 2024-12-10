package ru.practicum.shareit.item.dto;

import lombok.*;

/**
 * Класс ItemDtoRequest представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями
 * приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - requestId — идентификатор запроса, в ответ на который создана вещь.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}