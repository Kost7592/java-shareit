package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс Booking представляет запись о бронировании.
 * Он содержит следующие поля:
 * — id — уникальный идентификатор бронирования;
 * — start — дата и время начала бронирования;
 * — end — дата и время окончания бронирования;
 * — item — элемент, который был забронирован;
 * — booker — пользователь, сделавший бронирование;
 * — status — статус бронирования.
 */
@Data
@AllArgsConstructor
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
