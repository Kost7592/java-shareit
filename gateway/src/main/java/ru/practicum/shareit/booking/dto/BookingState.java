package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Определяет различные типы состояний бронирования.
 */
public enum BookingState {
    /**
     * Все бронирования
     */
    ALL,
    /**
     * Текущие бронирования
     */
    CURRENT,
    /**
     * Будущие бронирования
     */
    FUTURE,
    /**
     * Прошедшие бронирования
     */
    PAST,
    /**
     * Отклоненные бронирования
     */
    REJECTED,
    /**
     * Ожидающие подтверждения
     */
    WAITING;

    /**
     * Метод from преобразует строку в состояние бронирования
     * @param stringState статус бронирования.
     */
    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
