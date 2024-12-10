package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    /**
     * Метод добавляет новый запрос вещи (itemRequest) в систему.
     * @param userId  уникальный идентификатор пользователя, который добавляет элемент.
     * @param itemRequestDto объект с данными о новом элементе.
     */
    ItemRequestResponseDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    /**
     * Метод получает информацию о запросе на добавление элемента (item) в систему.
     * @param userId уникальный идентификатор пользователя, отправившего запрос.
     */
    List<ItemRequestResponseDto> getItemRequestsByUserId(long userId);

    /**
     * Метод получает информации обо всех запросах на добавление элементов (item requests) из системы
     * @param userId уникальный идентификатор пользователя, чьи запросы необходимо получить.
     * @param from смещение (с какого элемента начинать).
     * @param size количество элементов для получения.
     */
    List<ItemRequestResponseDto> getAllItemRequests(long userId, int from, int size);

    /**
     * Метод получает информации обо всех запросах на добавление элементов (item requests) в системе.
     * @param userId уникальный идентификатор пользователя, чьи запросы необходимо получить.
     */
    ItemRequestResponseDto getItemRequest(long requestId, long userId);
}
