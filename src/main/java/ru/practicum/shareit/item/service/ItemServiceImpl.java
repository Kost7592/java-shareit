package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс ItemServiceImpl реализация интерфейса ItemService для работы с вещами (items).
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto newItemDto) {
        Item newItem = ItemMapper.toItem(newItemDto, userId);
        return ItemMapper.toItemDto(itemRepository.createItem(userId, newItem));
    }

    @Override
    public ItemDto updateItem(Long userId, Long id, ItemDto updatedItemDto) {
        Item updatedItem = ItemMapper.toItem(updatedItemDto, userId);
        return ItemMapper.toItemDto(itemRepository.updateItem(userId, id, updatedItem));
    }

    @Override
    public ItemDto getItemDto(Long id) {
        return ItemMapper.toItemDto(itemRepository.getItem(id));
    }

    @Override
    public void deleteItem(Long id) {
    }

    @Override
    public List<ItemDto> getOwnerAllItems(Long ownerId) {
        List<Item> ownerItems = itemRepository.getOwnerItems(ownerId);
        return ownerItems.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> searchedItems = itemRepository.getItemsBySearch(text);
        return searchedItems.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
