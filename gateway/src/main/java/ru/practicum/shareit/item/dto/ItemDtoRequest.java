package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.validation.Create;

/**
 * Класс представляет собой DTO (Data Transfer Object) для вещи.
 * Содержит следующие поля:
 * - id - идентификатор вещи;
 * - name - название вещи;
 * - description - описание вещи;
 * - available - статус доступности вещи;
 * - ownerId - идентификатор владельца вещи;
 * - requestId - идентификатор запроса на вещь.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoRequest {
    private Long id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
