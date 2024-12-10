package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

/**
 * Класс ItemBookingDto представляет собой DTO (Data Transfer Object) для передачи данных о вещи между слоями
 * приложения.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор вещи;
 * - name — название вещи;
 * - description — описание вещи;
 * - available — статус доступности;
 * - ownerId — идентификатор пользователя-владельца;
 * - request — идентификатор запроса, в ответ на который создана вещь.
 */
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentDtoResponse> comments;
}