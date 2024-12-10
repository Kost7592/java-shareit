package ru.practicum.shareit.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс BadRequestException используется для обработки ситуации, когда во время валидации данных происходит ошибка.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Метод BadRequestException используется для обработки ситуации, когда во время валидации данных происходит ошибка.
     *
     * @param message сообщение об ошибке, которое будет выведено при возникновении исключения.
     */
    public BadRequestException(String message) {
        super(message);
    }
}