package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import ru.practicum.shareit.booking.validation.StartValid;

import java.time.LocalDateTime;

/**
 * Класс представляет собой DTO (Data Transfer Object) для запроса на бронирование. Он содержит информацию о начале и
 * конце бронирования, а также идентификатор товара.
 * - start — дата и время начала бронирования;
 * - end - дата и время окончания бронирования;
 * - itemId - идентификатор товара, который будет забронирован.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@StartValid
@Builder
public class BookingDtoRequest {
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
