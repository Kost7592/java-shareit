package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingLastAndNextDto;

import java.util.List;

/**
 * Класс ItemForBookingDto представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями
 * приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - lastBooking - последнее бронирование вещи;
 * - nextBooking - следующее бронирование вещи;
 * - comments - комментарии о вещи.
 */
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemForBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLastAndNextDto lastBooking;
    private BookingLastAndNextDto nextBooking;
    private final List<CommentDtoResponse> comments;
}
