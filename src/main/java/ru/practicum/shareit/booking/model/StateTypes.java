package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.error.exception.NotFoundException;

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
        for (StateTypes state : StateTypes.values()) {
            if (state.toString().equals(text)) {
                return state;
            }
        }
        throw new NotFoundException("Неизвестный статус");
    }
}
