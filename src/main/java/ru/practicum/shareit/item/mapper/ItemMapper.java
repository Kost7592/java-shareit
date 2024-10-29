package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс ItemMapper для маппинга объектов Item на объекты ItemDto и обратно.
 */
public class ItemMapper {
    /**
     * Метод преобразует объект Item в объект ItemDto.
     *
     * @param item объект типа Item, который нужно преобразовать.
     * @return новый объект типа ItemDto, содержащий данные из переданного объекта Item.
     */
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    /**
     * Метод преобразует объект ItemDto в объект Item.
     *
     * @param itemDto объект типа ItemDto, который нужно преобразовать.
     * @param ownerId идентификатор владельца, используется при создании нового объекта Item.
     * @return новый объект типа Item, содержащий данные из переданного объекта ItemDto.
     */
    public static Item toItem(ItemDto itemDto, long ownerId) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwnerId(),
                null);
    }
}
