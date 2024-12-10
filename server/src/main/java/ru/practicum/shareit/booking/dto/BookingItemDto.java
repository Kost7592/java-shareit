package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO-объект информации о бронировании товара.
 * - id — уникальный идентификатор бронирования;
 * - bookerId — идентификатор пользователя, сделавшего бронирование;
 * - start — дата и время начала бронирования;
 * - end — дата и время окончания бронирования;
 */
@Data
@AllArgsConstructor
public class BookingItemDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
