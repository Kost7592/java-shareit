package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс ErrorResponse представляет собой ответ об ошибке, который возвращается пользователю.
 * Он содержит сообщение об ошибке в виде строки.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
}