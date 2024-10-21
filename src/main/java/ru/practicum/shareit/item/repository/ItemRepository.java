package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Long userID, Item item);

    Item updateItem(Long userId, Long id, Item updatedItem);

    Item getItem(long itemId);

    List<Item> getOwnerItems(long userId);

    List<Item> getItemsBySearch(String text);
}
