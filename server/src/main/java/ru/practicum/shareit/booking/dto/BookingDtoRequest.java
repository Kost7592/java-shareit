package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO-объект запроса на бронирование.
 * Он содержит следующие поля:
 * - start — дата и время начала бронирования;
 * - end — дата и время окончания бронирования;
 * - itemId — идентификатор вещи, которая была забронирована;
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoRequest {
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}