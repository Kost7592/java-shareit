package ru.practicum.shareit.error;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Класс ErrorHandler обрабатывает исключения, возникающие в процессе работы приложения.
 * Он предоставляет централизованное место для обработки исключений и позволяет возвращать
 * согласованные ответы об ошибках пользователям.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обрабатывает исключение BadRequestException, возвращая ответ с кодом 400 (Bad Request).
     * В ответе содержится сообщение об ошибке, полученное из исключения.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRequestFailedException(
            final UnsupportedStatusException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return new ErrorMessage(e.getMessage());
    }

    /**
     * Обрабатывает исключение NotValidationException, возвращая ответ с кодом 400 (Bad Request).
     * В ответе содержится сообщение об ошибке, полученное из исключения.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    /**
     * Обрабатывает исключение ConstraintViolationException, возвращая ответ с кодом 400 (Bad Request).
     * В ответе содержится сообщение об ошибке, полученное из исключения.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    /**
     * Обрабатывает исключение ConstraintViolationException, возвращая ответ с кодом 400 (Bad Request).
     * В ответе содержится сообщение об ошибке, полученное из исключения.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }

    /**
     * Обрабатывает исключение ThrowableException, возвращая ответ с кодом 500 (Internal Server Error).
     * В ответе содержится сообщение об ошибке, полученное из исключения.
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowableException(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return Map.of("error", e.getMessage());
    }
}
