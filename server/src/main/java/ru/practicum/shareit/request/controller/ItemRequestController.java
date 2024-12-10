package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Контроллер ItemRequestController для работы с запросами на вещи.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    static final String userHeader = "X-Sharer-User-Id";
    private final ItemRequestService service;

    /**
     * Метод addItemRequest обрабатывает POST-запросы и предназначен для добавления нового запроса на вещь в систему.
     *
     * @param userId         идентификатор пользователя, содержащийся в заголовке запроса.
     * @param itemRequestDto DTO-объект запроса вещи.
     */
    @PostMapping
    public ItemRequestResponseDto addItemRequest(@RequestHeader(userHeader) long userId,
                                                 @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST запрос на создание запроса вещи");
        return service.addItemRequest(userId, itemRequestDto);
    }

    /**
     * Метод getItemRequestsByUserId обрабатывает GET-запросы и предназначен для получения всех запросов на вещи,
     * созданных пользователем с указанным идентификатором.
     *
     * @param userId идентификатор пользователя, содержащийся в заголовке запроса.
     */
    @GetMapping
    public List<ItemRequestResponseDto> getItemRequestsByUserId(@RequestHeader(userHeader) long userId) {
        log.info("GET запрос на получение всех созданных запросов вещей пользователя с ID {}", userId);
        return service.getItemRequestsByUserId(userId);
    }

    /**
     * Метод getAllItemRequests обрабатывает GET-запросы и предназначен для получения всех запросов на вещи,
     * созданных другими пользователями.
     * @param userId идентификатор пользователя, содержащийся в заголовке запроса.
     * @param from   номер начальной позиции в списке результатов
     * @param size   количество объектов, которые должны быть возвращены в ответе
     */
    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllItemRequests(
            @RequestHeader(userHeader) long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        log.info("GET запрос на получение всех запросов созданных другими пользователями");
        return service.getAllItemRequests(userId, from, size);
    }

    /**
     * Метод getItemRequest обрабатывает GET-запросы и предназначен для получения всех запросов на вещи
     * @param requestId идентификатор требуемого запроса на вещь.
     * @param userId идентификатор пользователя, содержащийся в заголовке запроса.
     */
    @GetMapping("/{request-id}")
    public ItemRequestResponseDto getItemRequest(@PathVariable("request-id") long requestId,
                                                 @RequestHeader(userHeader) long userId) {
        return service.getItemRequest(requestId, userId);
    }
}