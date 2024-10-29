package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс ItemService для работы с вещами (items).
 */
public interface ItemService {
    /**
     * Метод createItem создаёт новый элемент для указанного пользователя.
     *
     * @param userId     идентификатор пользователя, создающего элемент.
     * @param newItemDto объект нового элемента.
     * @return созданный элемент в формате DTO.
     */
    ItemDto createItem(Long userId, ItemDto newItemDto);

    /**
     * Метод updateItem обновляет существующий элемент.
     *
     * @param userId         идентификатор пользователя, который выполняет обновление.
     * @param id             идентификатор обновляемого элемента.
     * @param updatedItemDto обновлённый объект элемента.
     * @return обновлённый элемент в формате DTO.
     */
    ItemDto updateItem(Long userId, Long id, ItemDto updatedItemDto);

    /**
     * Метод getItemDto получает элемент по его идентификатору.
     *
     * @param id идентификатор элемента.
     * @return элемент в формате DTO.
     */
    ItemDto getItemDto(Long id);

    /**
     * Метод deleteItem удаляет элемент по его идентификатору.
     *
     * @param id идентификатор удаляемого элемента.
     */
    void deleteItem(Long id);

    /**
     * Метод getOwnerAllItems возвращает все элементы, принадлежащие указанному владельцу.
     *
     * @param ownerId идентификатор владельца элементов.
     * @return список элементов в формате DTO, принадлежащих указанному владельцу.
     */
    List<ItemDto> getOwnerAllItems(Long ownerId);

    /**
     * Метод getItemsBySearch ищет элементы по заданному тексту.
     *
     * @param text текст для поиска.
     * @return список найденных элементов в формате DTO.
     */
    List<ItemDto> getItemsBySearch(String text);
}
