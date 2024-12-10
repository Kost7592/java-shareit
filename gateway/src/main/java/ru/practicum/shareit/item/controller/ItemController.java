package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.validation.Create;

import java.util.Collections;

@Controller("GatewayItemController")
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    static final String userHeader = "X-Sharer-User-Id";
    static final String path = "/{item-id}";
    private final ItemClient itemClient;

    /**
     * Метод createItem создаёт новую вещь для указанного пользователя.
     *
     * @param userId  идентификатор пользователя, создающего вещь.
     * @param itemDto объект новой вещи.
     * @return созданная вещь в формате DTO.
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(userHeader) Long userId,
                                             @RequestBody @Validated({Create.class}) ItemDtoRequest itemDto) {
        log.info("POST запрос на создание вещи userId={}, itemDto={}", userId, itemDto);
        return itemClient.postItem(itemDto, userId);
    }

    /**
     * Метод addComment обрабатывает запросы на создание комментария к вещи.
     *
     * @param itemId            — идентификатор вещи, к которой будет добавлен комментарий.
     * @param userId            — идентификатор пользователя, который добавляет комментарий.
     * @param commentDto — объект с данными комментария.
     * @return объект CommentDtoResponse с информацией о добавленном комментарии.
     */
    @PostMapping("/{item-id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable("item-id") Long itemId,
                                             @RequestHeader(userHeader) Long userId,
                                             @Valid @RequestBody CommentDtoRequest commentDto) {
        log.info("POST запрос на создание комментария userId={}, itemId={}, commentDto={}", userId, itemId, commentDto);
        return itemClient.addComment(itemId, userId, commentDto);
    }

    /**
     * Метод updateItem обновляет существующую вещь.
     *
     * @param userId  идентификатор пользователя, который выполняет обновление.
     * @param itemId      идентификатор обновляемой вещи.
     * @param itemDto обновлённый объект вещи.
     * @return обновлённая вещь в формате DTO.
     */
    @PatchMapping(path)
    public ResponseEntity<Object> updateItem(@RequestBody ItemDtoRequest itemDto,
                                             @RequestHeader(userHeader) Long userId,
                                             @PathVariable("item-id") long itemId) {
        log.info("PATCH запрос на обновление вещи userId={}, itemId= {}, itemDto={}", userId, itemId, itemDto);
        return itemClient.patchItem(userId, itemId, itemDto);
    }

    /**
     * Метод getItemDto получает вещь по ее идентификатору.
     *
     * @param itemId идентификатор вещи;
     * @param ownerId идентификатор владельца;
     * @return вещь в формате DTO.
     */
    @GetMapping(path)
    public ResponseEntity<Object> getItemDto(@PathVariable("item-id") Long itemId,
                                             @RequestHeader(userHeader) Long ownerId) {
        log.info("GET запрос на получение вещи itemId={}, ownerId={}", itemId, ownerId);
        return itemClient.getItem(itemId, ownerId);
    }

    /**
     * Метод getOwnerItems возвращает все вещи, принадлежащие указанному владельцу.
     *
     * @param userId идентификатор владельца вещей.
     * @return список вещей в формате DTO, принадлежащих указанному владельцу.
     */
    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(userHeader) Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "20") Integer size) {
        log.info("GET запрос на получение всех вещей пользователя userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItemsUser(userId, from, size);
    }

    /**
     * Метод getItemsBySearch ищет вещи по заданному тексту.
     * @param userId идентификатор пользователя
     * @param text текст для поиска;
     * @param from номер начальной позиции в списке результатов;
     * @param size количество объектов, которые должны быть возвращены в ответе.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@RequestHeader(userHeader) long userId,
                                                   @RequestParam String text,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "20") Integer size) {
        log.info("Получил GET запрос на получение всех вещей с текстом:={}, from={}, size={}", text, from, size);
        if (text == null || text.isBlank()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return itemClient.getSearchOfText(userId, text, from, size);
    }
}