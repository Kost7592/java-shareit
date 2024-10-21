package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();
    private long idCount = 1;

    @Override
    public Item createItem(Long userId, Item newItem) {
        if (userRepository.getUserById(userId) == null) {
            log.error("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Владелец с id:" + userId + " не найден в пользователях");
        }
        newItem.setId(getNextId());
        newItem.setOwnerId(userId);
        items.put(newItem.getId(), newItem);
        log.info("Вещь добавлена");
        return newItem;
    }

    @Override
    public Item updateItem(Long userId, Long id, Item updatedItem) {
        if (!(items.containsKey(id))) {
            log.error("Вещь с id {} не найдена", id);
            throw new NotFoundException("Вещь с id:" + id + " не найдена");
        } else {
            Item oldItem = items.get(id);
            if (!oldItem.getOwnerId().equals(userId)) {
                throw new ValidationException("Пользователь не имеет доступ к вещи");
            }
            updateItemFields(id, updatedItem);
            log.info("Вещь с id {} обновлена", id);
            return items.get(id);
        }
    }

    @Override
    public Item getItem(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NotFoundException("Вещь с id:" + itemId + " не найдена");
        }
    }

    @Override
    public List<Item> getOwnerItems(long ownerId) {
        User owner = userRepository.getUserById(ownerId);
        if (owner == null) {
            log.error("Владелец с id: {} не найден", ownerId);
            throw new NotFoundException("\"Владелец с id:\" + userId + \" не найден в пользователях\"");
        } else {
            Collection<Item> ownerItems = items.values();
            List<Item> result = new ArrayList<>();
            ownerItems.stream().filter(item -> item.getOwnerId().equals(owner.getId())).forEach(result::add);
            log.info("Получен список вещей владельца с id: {}", ownerId);
            return result;
        }
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        List<Item> itemBySearch = new ArrayList<>();
        List<Item> itemsList = new ArrayList<>(items.values());
        for (Item item : itemsList) {
            if ((StringUtils.containsIgnoreCase(item.getName(), text) ||
                    StringUtils.containsIgnoreCase(item.getDescription(), text))
                    && item.getAvailable()) {
                itemBySearch.add(item);
            }
        }
        log.info("Получен список вещей по текстовому запросу");
        return itemBySearch;
    }

    private void updateItemFields(Long id, Item updatedItem) {
        Item item = items.get(id);
        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        items.put(id, item);
    }

    private long getNextId() {
        return idCount++;
    }
}
