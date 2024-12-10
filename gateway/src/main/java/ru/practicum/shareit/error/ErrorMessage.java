package ru.practicum.shareit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Класс ErrorMessage представляет собой ответ об ошибке, который возвращается пользователю.
 * Он содержит сообщение об ошибке в виде строки.
 */
@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    private final String error;
}
