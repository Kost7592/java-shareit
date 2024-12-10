package ru.practicum.shareit.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс NotFoundException используется для обработки ситуации, когда возникает конфликт из-за отсутствия данных.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Метод NotFoundException используется для обработки ситуации, когда возникает конфликт из-за отсутствия данных.
     *
     * @param message сообщение об ошибке, которое будет выведено при возникновении исключения.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
