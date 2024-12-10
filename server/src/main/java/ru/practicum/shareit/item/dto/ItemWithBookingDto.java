package ru.practicum.shareit.item.dto;

import lombok.*;

/**
 * Класс ItemWithBookingDto представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemWithBookingDto {
    private Long id;
    private String name;
}
