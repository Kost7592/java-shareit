package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * Класс Item представляет запись о вещи.
 * Он содержит следующие поля:
 * — id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - ownerId — идентификатор пользователя-владельца;
 * - request — запрос, в ответ на который создана вещь.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @NotBlank(message = "Поле не может быть пустым")
    private String description;
    @NotNull(message = "Поле не может быть null")
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
}
