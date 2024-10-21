package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto newItemDto);

    ItemDto updateItem(Long userId, Long id, ItemDto updatedItemDto);

    ItemDto getItemDto(Long id);

    void deleteItem(Long id);

    List<ItemDto> getOwnerAllItems(Long ownerId);

    List<ItemDto> getItemsBySearch(String text);
}
