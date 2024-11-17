package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.error.exception.NotFoundException;

import java.util.stream.Stream;

/**
 * Определяет различные типы состояний бронирования.
 */
public enum StateTypes {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    /**
     * Возвращает тип состояния на основе переданного текста.
     *
     * @param text текст, представляющий статус
     * @return соответствующий тип состояния из {@link StateTypes} или выбрасывает исключение {@link NotFoundException},
     * если статус не найден
     */
    public static StateTypes getStateFromText(String text) {
        return Stream.of(values())
                .filter(x -> x.name().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Неизвестный статус"));
    }
}
