package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс ItemDto представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - ownerId — идентификатор пользователя-владельца;
 * - requestId — идентификатор запроса, в ответ на который создана вещь.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @NotBlank(message = "Поле не может быть пустым")
    private String description;
    @NotNull(message = "Поле не может быть null")
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
