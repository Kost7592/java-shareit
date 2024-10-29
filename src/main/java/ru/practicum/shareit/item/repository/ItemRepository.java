package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс ItemRepository для работы с вещами (items).
 */
public interface ItemRepository {
    /**
     * Метод createItem создаёт новую вещь.
     *
     * @param userID идентификатор пользователя, который становится владельцем новой вещи.
     * @param item вещь, данные которой будут использоваться для создания нового объекта.
     * @return созданная вещь.
     */
    Item createItem(Long userID, Item item);

    /**
     * Метод updateItem обновляет существующую вещь.
     *
     * @param userId идентификатор пользователя, выполняющего обновление.
     * @param id идентификатор обновляемой вещи.
     * @param updatedItem обновлённая вещь.
     * @return обновлённая вещь.
     */
    Item updateItem(Long userId, Long id, Item updatedItem);

    /**
     * Метод getItem получает вещь по ее идентификатору.
     *
     * @param itemId идентификатор искомой вещи.
     * @return вещь с указанным идентификатором.
     * @throws NotFoundException, если вещь не найдена.
     */
    Item getItem(long itemId);

    /**
     * Метод getOwnerItems возвращает все вещи, принадлежащие указанному пользователю.
     *
     * @param userId идентификатор пользователя.
     * @return список вещей, принадлежащих указанному пользователю, или пустой список, если у пользователя
     * нет вещей.
     */
    List<Item> getOwnerItems(long userId);

    /**
     * Метод getItemsBySearch ищет вещи по тексту.
     *
     * @param text текст для поиска.
     * @return список найденных вещей или пустой список, если вещи не найдены.
     */
    List<Item> getItemsBySearch(String text);
}
