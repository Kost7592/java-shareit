package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Контроллер ItemController для работы с элементами (items).
 */
@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Метод createItem создаёт новую вещь для указанного пользователя.
     *
     * @param userId  идентификатор пользователя, создающего вещь.
     * @param itemDto объект новой вещи.
     * @return созданная вещь в формате DTO.
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание вещи владельцем с id: {}", userId);
        return itemService.createItem(userId, itemDto);
    }

    /**
     * Метод updateItem обновляет существующую вещь.
     *
     * @param userId  идентификатор пользователя, который выполняет обновление.
     * @param id      идентификатор обновляемой вещи.
     * @param itemDto обновлённый объект вещи.
     * @return обновлённая вещь в формате DTO.
     */
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление вещи c id: {}", id);
        return itemService.updateItem(userId, id, itemDto);
    }

    /**
     * Метод getItemDto получает вещь по ее идентификатору.
     *
     * @param id идентификатор вещи.
     * @return вещь в формате DTO.
     */
    @GetMapping("/{id}")
    public ItemDto getItemDto(@PathVariable Long id) {
        log.info("Получен запрос на получение вещи c id: {}", id);
        return itemService.getItemDto(id);
    }

    /**
     * Метод getOwnerItems возвращает все вещи, принадлежащие указанному владельцу.
     *
     * @param ownerId идентификатор владельца вещей.
     * @return список вещей в формате DTO, принадлежащих указанному владельцу.
     */
    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Получен запрос на получение вещей владельца с id:{}", ownerId);
        return itemService.getOwnerAllItems(ownerId);
    }

    /**
     * Метод getItemsBySearch ищет вещи по заданному тексту.
     *
     * @param text текст для поиска.
     * @return список найденных вещей в формате DTO.
     */
    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("Получен запрос на поиск вещи по тексту");
        return itemService.getItemsBySearch(text);
    }
}
