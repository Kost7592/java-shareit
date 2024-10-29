package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс ValidationException используется для обработки ситуации, когда во время валидации данных происходит ошибка.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ValidationException extends RuntimeException {

    /**
     * Метод ValidationException используется для обработки ситуации, когда во время валидации данных происходит ошибка.
     *
     * @param message сообщение об ошибке, которое будет выведено при возникновении исключения.
     */
    public ValidationException(String message) {
        super(message);
    }
}
