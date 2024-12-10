package ru.practicum.shareit.booking.model;

/**
 * Перечисление BookingStatus представляет статусы бронирования.
 */
public enum BookingStatus {
    /**
     * Бронирование ожидает подтверждения.
     */
    WAITING,
    /**
     * Бронирование подтверждено.
     */
    APPROVED,
    /**
     * Бронирование отклонено.
     */
    REJECTED,
    /**
     * Бронирование отменено.
     */
    CANCELED
}
