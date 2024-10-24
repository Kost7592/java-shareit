package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс DuplicateException используется для обработки ситуации, когда возникает конфликт из-за дублирования данных.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException {

    /**
     * Метод DuplicateException используется для обработки ситуации, когда возникает конфликт из-за дублирования данных.
     *
     * @param message сообщение об ошибке, которое будет выведено при возникновении исключения.
     */
    public DuplicateException(String message) {
        super(message);
    }

}
