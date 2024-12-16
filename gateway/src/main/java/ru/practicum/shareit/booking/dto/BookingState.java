package ru.practicum.shareit.booking.dto;

import java.util.Arrays;
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
        return Arrays.stream(BookingState.values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
