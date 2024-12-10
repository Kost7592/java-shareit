package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс ItemService для работы с вещами (items).
 */
public interface ItemService {
    /**
     * Метод createItem создаёт новую вещь для указанного пользователя.
     *
     * @param userId     идентификатор пользователя, создающего вещь.
     * @param newItemDto объект новой вещи.
     * @return созданная вещь в формате DTO.
     */
    ItemDto createItem(Long userId, ItemDto newItemDto);

    /**
     * Метод updateItem обновляет существующую вещь.
     *
     * @param userId         идентификатор пользователя, который выполняет обновление.
     * @param itemId            идентификатор обновляемой вещи.
     * @param updatedItemDto обновлённый объект вещи.
     * @return обновлённая вещь в формате DTO.
     */
    ItemDto updateItem(Long userId, Long itemId, ItemDto updatedItemDto);

    /**
     * Метод getItemDto получает вещь по его идентификатору.
     *
     * @param id идентификатор вещи.
     * @return вещь в формате DTO.
     */

    ItemBookingDto getItemDto(Long ownerId, Long id);

    /**
     * Метод deleteItem удаляет вещь по ее идентификатору.
     *
     * @param id идентификатор удаляемой вещи.
     */
    void deleteItem(Long id);

    /**
     * Метод getOwnerAllItems возвращает все вещи, принадлежащие указанному владельцу.
     *
     * @param ownerId идентификатор владельца вещей.
     * @return список вещей в формате DTO, принадлежащих указанному владельцу.
     */
    List<ItemDto> getOwnerAllItems(Long ownerId);

    /**
     * Метод getItemsBySearch ищет вещь по заданному тексту.
     *
     * @param text текст для поиска.
     * @return список найденных вещей в формате DTO.
     */
    List<ItemDto> getItemsBySearch(String text);

    /**
     * Метод addComment добавляет комментарий для вещи.
     *
     * @param itemId текст для поиска.
     * @param userId идентификатор автора комментария.
     * @param commentDtoRequest объект комментария commentDtoRequest приходящий в запросе CommentDtoRequest.
     * @return список найденных вещей в формате DTO.
     */
    CommentDtoResponse addComment(long itemId, long userId, CommentDtoRequest commentDtoRequest);
}
