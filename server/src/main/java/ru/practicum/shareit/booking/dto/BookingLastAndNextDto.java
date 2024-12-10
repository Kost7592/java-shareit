package ru.practicum.shareit.booking.dto;

import lombok.*;

/**
 * DTO-объект информации о бронировании товара.
 * - id — уникальный идентификатор бронирования;
 * - bookerId — идентификатор пользователя, сделавшего бронирование;
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class BookingLastAndNextDto {
    private Long id;
    private Long bookerId;
}
