package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.dto.UserWithIdDto;

import java.time.LocalDateTime;

/**
 * DTO-объект ответа на бронирование.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор бронирования;
 * - start — дата и время начала бронирования;
 * - end — дата и время окончания бронирования;
 * - item — вещь, которая была забронирована;
 * - booker — пользователь, сделавший бронирование;
 * - status — статус бронирования.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemWithBookingDto item;
    private UserWithIdDto booker;
    private BookingStatus status;
}