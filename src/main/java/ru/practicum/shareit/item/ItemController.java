package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание вещи владельцем с id: {}", userId);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи c id: {}", id);
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItemDto(@PathVariable Long id) {
        log.info("Получен запрос на получение вещи c id: {}", id);
        return itemService.getItemDto(id);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Получен запрос на получение вещей владельца с id:{}", ownerId);
        return itemService.getOwnerAllItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("Получен запрос на поиск вещи по тексту");
        return itemService.getItemsBySearch(text);
    }
}
