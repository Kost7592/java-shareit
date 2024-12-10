package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

/**
 * Класс представляет собой контроллер для работы с запросами на добавление элементов (item requests) в систему.
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    static final String userHeader = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    /**
     * Метод отправляет запрос на добавление нового элемента (item) в систему.
     * @param userId уникальный идентификатор пользователя, который добавляет элемент.
     * @param itemRequestDto объект с данными о новом элементе.
     */
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(userHeader) Long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST на добавление запроса userId={}, itemRequestDto={}", userId, itemRequestDto);
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    /**
     * Метод отправляет запрос на получение информации обо всех запросах на добавление элементов (item requests) в
     * систему пользователем с уникальным идентификатором userId.
     * @param userId уникальный идентификатор пользователя.
     */
    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUserId(
            @RequestHeader(userHeader) Long userId) {
        log.info("GET на запросы пользователя userId={}", userId);
        return itemRequestClient.getItemRequestsByUserId(userId);
    }

    /**
     * Метод отправляет запрос на получение информации обо всех запросах на добавление элементов (item requests) в
     * систему
     * @param userId уникальный идентификатор пользователя.
     * @param from смещение (с какого элемента начинать).
     * @param size количество элементов для получения.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(userHeader) Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET на все запросы  userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    /**
     * Метод отправляет запрос на получение информации о запросе на добавление элемента (item) в систему с идентификатором requestId
     * @param requestId уникальный идентификатор запроса на вещь.
     * @param userId уникальный идентификатор пользователя
     */
    @GetMapping("{request-id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable("request-id") Long requestId,
                                                 @RequestHeader(userHeader) Long userId) {
        log.info("GET userId={}, requestId={}", userId, requestId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }
}
