package ru.practicum.shareit.error;

/**
 * Класс представляет собой исключение, которое выбрасывается при попытке обработать неподдерживаемый статус.
 */
public class UnsupportedStatusException extends RuntimeException {

    public UnsupportedStatusException(String message) {
        super(message);
    }
}

