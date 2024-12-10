package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс BookingDto представляет собой DTO (Data Transfer Object) для передачи данных о бронировании.
 * Он содержит следующие поля:
 * - id — уникальный идентификатор бронирования;
 * - start — дата и время начала бронирования;
 * - end — дата и время окончания бронирования;
 * - item — объект, представляющий вещь, который был забронирован;
 * - booker — пользователь, сделавший бронирование;
 * - status — статус бронирования.
 */
@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
